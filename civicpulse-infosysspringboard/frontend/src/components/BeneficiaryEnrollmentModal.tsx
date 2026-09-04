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

interface BeneficiaryEnrollmentModalProps {
  open: boolean;
  onClose: () => void;
  onSuccess: (newBeneficiary: any) => void;
  schemeId: string;
}

export default function BeneficiaryEnrollmentModal({
  open,
  onClose,
  onSuccess,
  schemeId
}: BeneficiaryEnrollmentModalProps) {
  const [citizens, setCitizens] = useState<any[]>([]);
  const [selectedCitizenId, setSelectedCitizenId] = useState('');
  const [eligibilityCriteria, setEligibilityCriteria] = useState('');
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

    if (!selectedCitizenId || !eligibilityCriteria) {
      setError('Please fill in all fields.');
      return;
    }

    const citizen = citizens.find(c => c.id === selectedCitizenId);
    if (!citizen) {
      setError('Citizen not found.');
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/v1/welfare/schemes/${schemeId}/enroll`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          citizenId: citizen.id,
          citizenName: citizen.name,
          eligibilityCriteria
        }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.error || 'Failed to enroll beneficiary.');
      }

      onSuccess(data);
      setEligibilityCriteria('');
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
          Enroll Beneficiary
        </Typography>
        <IconButton onClick={onClose} sx={{ color: 'var(--text-muted)' }}>
          <CloseIcon />
        </IconButton>
      </DialogTitle>

      <form onSubmit={handleSubmit}>
        <DialogContent dividers sx={{ borderColor: 'var(--border-color)', display: 'flex', flexDirection: 'column', gap: 2.5 }}>
          {error && <Alert severity="error">{error}</Alert>}

          <FormControl fullWidth variant="outlined">
            <InputLabel id="beneficiary-citizen-label">Select Citizen *</InputLabel>
            <Select
              labelId="beneficiary-citizen-label"
              value={selectedCitizenId}
              onChange={(e) => setSelectedCitizenId(e.target.value)}
              label="Select Citizen *"
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

          <TextField
            label="Eligibility Criteria Verification Comments *"
            variant="outlined"
            fullWidth
            value={eligibilityCriteria}
            onChange={(e) => setEligibilityCriteria(e.target.value)}
            disabled={loading}
            placeholder="e.g. Household annual income below threshold."
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
            {loading ? 'Enrolling...' : 'Enroll'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
