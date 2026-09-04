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

interface GrievanceRegistrationModalProps {
  open: boolean;
  onClose: () => void;
  onSuccess: (newGrievance: any) => void;
}

export default function GrievanceRegistrationModal({
  open,
  onSuccess,
  onClose
}: GrievanceRegistrationModalProps) {
  const [citizens, setCitizens] = useState<any[]>([]);
  const [selectedCitizenId, setSelectedCitizenId] = useState('');
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [category, setCategory] = useState('WATER_SUPPLY');
  const [severity, setSeverity] = useState('MEDIUM');
  const [location, setLocation] = useState('');
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

    if (!selectedCitizenId || !title || !description || !location) {
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
      const response = await fetch('http://localhost:8080/api/v1/grievances', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          citizenId: citizen.id,
          citizenName: citizen.name,
          title,
          description,
          category,
          severity,
          location,
          ward: citizen.ward
        }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || 'Failed to submit grievance.');
      }

      onSuccess(data);
      setTitle('');
      setDescription('');
      setLocation('');
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
          maxWidth: '500px',
          width: '100%',
          boxShadow: '0 10px 30px rgba(0,0,0,0.1)'
        }
      }}
    >
      <DialogTitle sx={{ m: 0, p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h6" sx={{ fontWeight: 'bold', color: 'var(--primary)' }}>
          File Official Grievance
        </Typography>
        <IconButton onClick={onClose} sx={{ color: 'var(--text-muted)' }}>
          <CloseIcon />
        </IconButton>
      </DialogTitle>

      <form onSubmit={handleSubmit}>
        <DialogContent dividers sx={{ borderColor: 'var(--border-color)', display: 'flex', flexDirection: 'column', gap: 2.5 }}>
          {error && <Alert severity="error">{error}</Alert>}

          <FormControl fullWidth variant="outlined">
            <InputLabel id="grievance-citizen-select">Select Submitting Citizen *</InputLabel>
            <Select
              labelId="grievance-citizen-select"
              value={selectedCitizenId}
              onChange={(e) => setSelectedCitizenId(e.target.value)}
              label="Select Submitting Citizen *"
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
            label="Grievance Title *"
            variant="outlined"
            fullWidth
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            disabled={loading}
            placeholder="e.g. Broken Water Pipe / Streetlight Outage"
          />

          <TextField
            label="Detailed Complaint Description *"
            variant="outlined"
            fullWidth
            multiline
            rows={3}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            disabled={loading}
            placeholder="Describe the problem, duration, and local impact..."
          />

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <FormControl fullWidth variant="outlined">
              <InputLabel id="category-label">Category *</InputLabel>
              <Select
                labelId="category-label"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                label="Category *"
              >
                <MenuItem value="WATER_SUPPLY">Water Supply</MenuItem>
                <MenuItem value="SEWAGE_AND_DRAINAGE">Sewage &amp; Drainage</MenuItem>
                <MenuItem value="WASTE_MANAGEMENT">Waste Management</MenuItem>
                <MenuItem value="ROADS_AND_BRIDGES">Roads &amp; Bridges</MenuItem>
                <MenuItem value="STREETLIGHTS">Streetlights</MenuItem>
                <MenuItem value="PUBLIC_HEALTH">Public Health</MenuItem>
              </Select>
            </FormControl>

            <FormControl fullWidth variant="outlined">
              <InputLabel id="severity-label">Severity *</InputLabel>
              <Select
                labelId="severity-label"
                value={severity}
                onChange={(e) => setSeverity(e.target.value)}
                label="Severity *"
              >
                <MenuItem value="LOW">Low (Routine)</MenuItem>
                <MenuItem value="MEDIUM">Medium (Normal)</MenuItem>
                <MenuItem value="HIGH">High (Urgent)</MenuItem>
                <MenuItem value="CRITICAL">Critical (Immediate Action)</MenuItem>
              </Select>
            </FormControl>
          </div>

          <TextField
            label="Incident Location / Landmark *"
            variant="outlined"
            fullWidth
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            disabled={loading}
            placeholder="e.g. Lane 3, near Central Park"
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
