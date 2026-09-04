import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Typography,
  Alert,
  IconButton
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';

interface CitizenRegistrationModalProps {
  open: boolean;
  onClose: () => void;
  onSuccess: (newCitizen: any) => void;
}

export default function CitizenRegistrationModal({
  open,
  onClose,
  onSuccess
}: CitizenRegistrationModalProps) {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [aadharNumber, setAadharNumber] = useState('');
  const [ward, setWard] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!name || !email || !phone || !aadharNumber || !ward) {
      setError('Please fill in all fields.');
      return;
    }

    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/v1/citizens', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, email, phone, aadharNumber, ward }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || 'Failed to register citizen.');
      }

      onSuccess(data);
      setName('');
      setEmail('');
      setPhone('');
      setAadharNumber('');
      setWard('');
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
          Register Citizen Profile
        </Typography>
        <IconButton onClick={onClose} sx={{ color: 'var(--text-muted)' }}>
          <CloseIcon />
        </IconButton>
      </DialogTitle>

      <form onSubmit={handleSubmit}>
        <DialogContent dividers sx={{ borderColor: 'var(--border-color)', display: 'flex', flexDirection: 'column', gap: 2.5 }}>
          {error && <Alert severity="error">{error}</Alert>}

          <TextField
            label="Full Name *"
            variant="outlined"
            fullWidth
            value={name}
            onChange={(e) => setName(e.target.value)}
            disabled={loading}
            placeholder="e.g. Vikram Malhotra"
          />

          <TextField
            label="Email Address *"
            variant="outlined"
            fullWidth
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            disabled={loading}
            placeholder="e.g. vikram@example.com"
          />

          <TextField
            label="Phone Number *"
            variant="outlined"
            fullWidth
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            disabled={loading}
            placeholder="e.g. +91-9876543210"
          />

          <TextField
            label="Aadhar ID Number *"
            variant="outlined"
            fullWidth
            value={aadharNumber}
            onChange={(e) => setAadharNumber(e.target.value)}
            disabled={loading}
            placeholder="e.g. 1122-3344-5566"
          />

          <TextField
            label="Ward Number / Area *"
            variant="outlined"
            fullWidth
            value={ward}
            onChange={(e) => setWard(e.target.value)}
            disabled={loading}
            placeholder="e.g. Ward 4"
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
            disabled={loading}
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
            {loading ? 'Registering...' : 'Register'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
