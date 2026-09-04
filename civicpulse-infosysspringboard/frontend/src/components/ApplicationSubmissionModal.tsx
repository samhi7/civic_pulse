import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Typography,
  Alert,
  IconButton,
  Select,
  MenuItem,
  FormControl,
  InputLabel
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';

interface ApplicationSubmissionModalProps {
  open: boolean;
  onClose: () => void;
  onSuccess: (newApplication: any) => void;
}

export default function ApplicationSubmissionModal({
  open,
  onClose,
  onSuccess
}: ApplicationSubmissionModalProps) {
  const [citizens, setCitizens] = useState<any[]>([]);
  const [selectedCitizenId, setSelectedCitizenId] = useState('');
  const [type, setType] = useState('BIRTH_CERTIFICATE');
  
  // Dynamic metadata fields
  const [metaField1, setMetaField1] = useState('');
  const [metaField2, setMetaField2] = useState('');
  
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (open) {
      fetchCitizens();
    }
  }, [open]);

  const fetchCitizens = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/citizens');
      if (response.ok) {
        const data = await response.json();
        setCitizens(data);
        if (data.length > 0) {
          setSelectedCitizenId(data[0].id);
        }
      }
    } catch (err) {
      console.error('Error fetching citizens:', err);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!selectedCitizenId || !metaField1 || !metaField2) {
      setError('Please fill in all metadata fields.');
      return;
    }

    const citizen = citizens.find(c => c.id === selectedCitizenId);
    if (!citizen) {
      setError('Selected citizen not found.');
      return;
    }

    let metadataStr = '';
    if (type === 'BIRTH_CERTIFICATE' || type === 'DEATH_CERTIFICATE') {
      metadataStr = `Subject Name: ${metaField1}, Date: ${metaField2}`;
    } else if (type === 'TRADE_LICENSE') {
      metadataStr = `Business: ${metaField1}, Type: ${metaField2}`;
    } else {
      metadataStr = `Detail: ${metaField1}, Value: ${metaField2}`;
    }

    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/v1/services/applications', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          citizenId: citizen.id,
          citizenName: citizen.name,
          type,
          metadata: metadataStr
        }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error('Failed to submit service application.');
      }

      onSuccess(data);
      setMetaField1('');
      setMetaField2('');
      onClose();
    } catch (err: any) {
      setError(err.message || 'An error occurred.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      sx={{
        '& .MuiDialog-paper': {
          background: '#ffffff',
          border: '1px solid var(--border-color)',
          borderRadius: '16px',
          color: 'var(--text-main)',
          padding: '8px',
          maxWidth: '480px',
          width: '100%',
          boxShadow: '0 10px 30px rgba(0,0,0,0.1)'
        }
      }}
    >
      <DialogTitle sx={{ m: 0, p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h6" sx={{ fontWeight: 'bold', color: 'var(--primary)' }}>
          Submit Service Application
        </Typography>
        <IconButton onClick={onClose} sx={{ color: 'var(--text-muted)' }}>
          <CloseIcon />
        </IconButton>
      </DialogTitle>

      <form onSubmit={handleSubmit}>
        <DialogContent dividers sx={{ borderColor: 'var(--border-color)', display: 'flex', flexDirection: 'column', gap: 2.5 }}>
          {error && <Alert severity="error">{error}</Alert>}

          <FormControl fullWidth variant="outlined">
            <InputLabel id="citizen-select-label">Applicant Citizen *</InputLabel>
            <Select
              labelId="citizen-select-label"
              value={selectedCitizenId}
              onChange={(e) => setSelectedCitizenId(e.target.value)}
              label="Applicant Citizen *"
            >
              {citizens.map(c => (
                <MenuItem key={c.id} value={c.id}>
                  {c.name} ({c.aadharNumber})
                </MenuItem>
              ))}
              {citizens.length === 0 && (
                <MenuItem disabled value="">
                  No registered citizens found.
                </MenuItem>
              )}
            </Select>
          </FormControl>

          <FormControl fullWidth variant="outlined">
            <InputLabel id="app-type-label">Service Document Type *</InputLabel>
            <Select
              labelId="app-type-label"
              value={type}
              onChange={(e) => setType(e.target.value)}
              label="Service Document Type *"
            >
              <MenuItem value="BIRTH_CERTIFICATE">Birth Certificate</MenuItem>
              <MenuItem value="DEATH_CERTIFICATE">Death Certificate</MenuItem>
              <MenuItem value="INCOME_CERTIFICATE">Income Certificate</MenuItem>
              <MenuItem value="RESIDENCE_CERTIFICATE">Residence Certificate</MenuItem>
              <MenuItem value="TRADE_LICENSE">Trade License (Permit)</MenuItem>
            </Select>
          </FormControl>

          <TextField
            label={type === 'TRADE_LICENSE' ? 'Business Name *' : 'Subject Name (e.g. child name, applicant name) *'}
            variant="outlined"
            fullWidth
            value={metaField1}
            onChange={(e) => setMetaField1(e.target.value)}
            disabled={loading}
            placeholder={type === 'TRADE_LICENSE' ? 'e.g. Ramesh General Store' : 'e.g. Aarav'}
          />

          <TextField
            label={type === 'TRADE_LICENSE' ? 'Business Type *' : (type === 'BIRTH_CERTIFICATE' || type === 'DEATH_CERTIFICATE' ? 'Event Date (e.g. 15-May-2026) *' : 'Annual Income / Detail *')}
            variant="outlined"
            fullWidth
            value={metaField2}
            onChange={(e) => setMetaField2(e.target.value)}
            disabled={loading}
            placeholder={type === 'TRADE_LICENSE' ? 'e.g. Retail, Service' : 'e.g. 15-May-2026'}
          />
        </DialogContent>

        <DialogActions sx={{ p: 2, display: 'flex', gap: 1 }}>
          <Button
            onClick={onClose}
            sx={{ color: 'var(--text-muted)', textTransform: 'none' }}
            disabled={loading}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            variant="contained"
            disabled={loading || citizens.length === 0}
            sx={{
              background: 'var(--gradient-teal)',
              color: '#fff',
              fontWeight: 'bold',
              textTransform: 'none',
              borderRadius: '8px',
              px: 3,
              boxShadow: 'none'
            }}
          >
            {loading ? 'Submitting...' : 'Submit'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
