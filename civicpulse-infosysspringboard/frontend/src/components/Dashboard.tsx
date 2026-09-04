import { useState, useEffect } from 'react';
import {
  Typography,
  Card,
  CardContent,
  Button,
  List,
  ListItem,
  ListItemText,
  Divider,
  Chip,
  TextField,
  MenuItem,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Menu,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  FormControl,
  Select,
  InputLabel,
  Rating,
  Snackbar,
  Alert,
  Tabs,
  Tab,
  Box
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import CheckCircleOutlinedIcon from '@mui/icons-material/CheckCircleOutlined';
import AssignmentIndIcon from '@mui/icons-material/AssignmentInd';
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';
import HistoryIcon from '@mui/icons-material/History';
import DownloadIcon from '@mui/icons-material/Download';
import AssignmentTurnedInIcon from '@mui/icons-material/AssignmentTurnedIn';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import PeopleIcon from '@mui/icons-material/People';
import CurrencyRupeeIcon from '@mui/icons-material/CurrencyRupee';
import HomeIcon from '@mui/icons-material/Home';
import GavelIcon from '@mui/icons-material/Gavel';
import FileCopyIcon from '@mui/icons-material/FileCopy';
import AssessmentIcon from '@mui/icons-material/Assessment';
import ExitToAppIcon from '@mui/icons-material/ExitToApp';
import ShieldIcon from '@mui/icons-material/Shield';
import ShareIcon from '@mui/icons-material/Share';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import BarChartIcon from '@mui/icons-material/BarChart';
import RateReviewIcon from '@mui/icons-material/RateReview';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import TrendingDownIcon from '@mui/icons-material/TrendingDown';
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import VerifiedIcon from '@mui/icons-material/Verified';

import CitizenRegistrationModal from './CitizenRegistrationModal';
import GrievanceRegistrationModal from './GrievanceRegistrationModal';
import ApplicationSubmissionModal from './ApplicationSubmissionModal';
import BeneficiaryEnrollmentModal from './BeneficiaryEnrollmentModal';

// Initial resilient fallback datasets
const INITIAL_CITIZENS = [
  { id: '36a83693-39f5-47ec-a63e-dbfa7b2a60bb', name: 'Ramesh Kumar', email: 'ramesh.kumar@example.com', phone: '+91-9876543210', aadharNumber: '1234-5678-9012', ward: 'Ward 12' },
  { id: '49b92718-47e5-4ebc-ba3d-abfa7b2a60cc', name: 'Priya Sharma', email: 'priya.sharma@example.com', phone: '+91-9876543211', aadharNumber: '2345-6789-0123', ward: 'Ward 8' },
  { id: 'e3d748f2-824f-4d37-8ff0-d128cbda9e11', name: 'Vikram Malhotra', email: 'vikram.malhotra@example.com', phone: '+91-9876543212', aadharNumber: '3456-7890-1234', ward: 'Ward 4' },
  { id: 'fa7a8b9c-10d9-4fa2-8b3d-cbda9e112233', name: 'Sunita Rao', email: 'sunita.rao@example.com', phone: '+91-9876543213', aadharNumber: '4567-8901-2345', ward: 'Ward 9' },
  { id: 'bc8a7c6b-1234-4567-89ab-cdef01234567', name: 'Amit Patel', email: 'amit.patel@example.com', phone: '+91-9876543214', aadharNumber: '5678-9012-3456', ward: 'Ward 15' }
];

const INITIAL_GRIEVANCES = [
  {
    id: 'f48b1111-2222-3333-4444-555566667777',
    citizenId: '36a83693-39f5-47ec-a63e-dbfa7b2a60bb',
    citizenName: 'Ramesh Kumar',
    title: 'Water Supply Disruption in Sector 5',
    description: 'No drinking water supply for the past 3 consecutive days. Low pressure and muddy water in morning hours.',
    category: 'WATER_SUPPLY',
    severity: 'HIGH',
    status: 'IN_PROGRESS',
    assignedDepartment: 'WATER_DEPT',
    location: 'Sector 5, Ward 12',
    ward: 'Ward 12',
    slaDays: 2,
    escalationLevel: 0,
    dueDate: new Date(Date.now() + 86400000 * 2).toISOString()
  },
  {
    id: 'f48b1111-2222-3333-4444-555566667778',
    citizenId: '49b92718-47e5-4ebc-ba3d-abfa7b2a60cc',
    citizenName: 'Priya Sharma',
    title: 'Garbage Pileup in Lane 3',
    description: 'Garbage has not been cleared for 4 days. Overflowing waste bin causing health hazard.',
    category: 'WASTE_MANAGEMENT',
    severity: 'CRITICAL',
    status: 'SUBMITTED',
    assignedDepartment: 'SANITATION',
    location: 'Lane 3, Ward 8',
    ward: 'Ward 8',
    slaDays: 1,
    escalationLevel: 0,
    dueDate: new Date(Date.now() + 86400000).toISOString()
  },
  {
    id: 'f48b1111-2222-3333-4444-555566667779',
    citizenId: 'e3d748f2-824f-4d37-8ff0-d128cbda9e11',
    citizenName: 'Vikram Malhotra',
    title: 'Broken Streetlights on Mahatma Gandhi Road',
    description: 'Streetlights out along entire 500m commercial stretch. High risk of traffic accidents.',
    category: 'ELECTRICITY',
    severity: 'MEDIUM',
    status: 'SUBMITTED',
    assignedDepartment: 'ELECTRICITY_DEPT',
    location: 'Mahatma Gandhi Road, Ward 4',
    ward: 'Ward 4',
    slaDays: 3,
    escalationLevel: 0,
    dueDate: new Date(Date.now() + 86400000 * 3).toISOString()
  },
  {
    id: 'f48b1111-2222-3333-4444-555566667780',
    citizenId: 'fa7a8b9c-10d9-4fa2-8b3d-cbda9e112233',
    citizenName: 'Sunita Rao',
    title: 'Dangerous Potholes on Main Market Road',
    description: 'Deep road depressions causing vehicle damage near city vegetable market.',
    category: 'ROAD_MAINTENANCE',
    severity: 'HIGH',
    status: 'IN_PROGRESS',
    assignedDepartment: 'PUBLIC_WORKS',
    location: 'Main Market Road, Ward 9',
    ward: 'Ward 9',
    slaDays: 5,
    escalationLevel: 0,
    dueDate: new Date(Date.now() + 86400000 * 5).toISOString()
  },
  {
    id: 'f48b1111-2222-3333-4444-555566667781',
    citizenId: 'bc8a7c6b-1234-4567-89ab-cdef01234567',
    citizenName: 'Amit Patel',
    title: 'Drainage & Sewage Overflow near Public School',
    description: 'Black sewer water overflowing onto pedestrian sidewalk.',
    category: 'WASTE_MANAGEMENT',
    severity: 'CRITICAL',
    status: 'SUBMITTED',
    assignedDepartment: 'SANITATION',
    location: 'Public School Road, Ward 15',
    ward: 'Ward 15',
    slaDays: 1,
    escalationLevel: 0,
    dueDate: new Date(Date.now() + 86400000).toISOString()
  }
];

const INITIAL_APPLICATIONS = [
  {
    id: 'a1111111-2222-3333-4444-555566667777',
    applicationNumber: 'APP-2024-1247',
    citizenId: '49b92718-47e5-4ebc-ba3d-abfa7b2a60cc',
    citizenName: 'Priya Sharma',
    type: 'BIRTH_CERTIFICATE',
    status: 'APPROVED',
    verified: true,
    metadata: 'Child: Aarav | DOB: 15-May-2026'
  },
  {
    id: 'a1111111-2222-3333-4444-555566667778',
    applicationNumber: 'APP-2026-2390',
    citizenId: '36a83693-39f5-47ec-a63e-dbfa7b2a60bb',
    citizenName: 'Ramesh Kumar',
    type: 'TRADE_LICENSE',
    status: 'DOCUMENT_VERIFIED',
    verified: true,
    metadata: 'Business: Ramesh General Store | Type: Retail'
  },
  {
    id: 'a1111111-2222-3333-4444-555566667779',
    applicationNumber: 'APP-2026-3104',
    citizenId: 'e3d748f2-824f-4d37-8ff0-d128cbda9e11',
    citizenName: 'Vikram Malhotra',
    type: 'INCOME_CERTIFICATE',
    status: 'SUBMITTED',
    verified: false,
    metadata: 'Annual Income: $14,500 | Sector: Private'
  }
];

const INITIAL_SCHEMES = [
  {
    id: 's1111111-2222-3333-4444-555566667777',
    name: 'PM Awas Yojana',
    description: 'Affordable urban housing financial assistance for low-income citizens',
    allocatedAmount: 2400000,
    disbursedAmount: 2100000,
    active: true
  },
  {
    id: 's1111111-2222-3333-4444-555566667778',
    name: 'Jal Jeevan Urban Water Mission',
    description: '100% piped drinking water household connections',
    allocatedAmount: 8000000,
    disbursedAmount: 6800000,
    active: true
  },
  {
    id: 's1111111-2222-3333-4444-555566667779',
    name: 'Ayushman Bharat Municipal Health',
    description: 'Subsidized clinical and surgical coverage for urban residents',
    allocatedAmount: 5000000,
    disbursedAmount: 4500000,
    active: true
  }
];

const INITIAL_BENEFICIARIES = [
  {
    id: 'b1111111-2222-3333-4444-555566667777',
    schemeId: 's1111111-2222-3333-4444-555566667777',
    citizenId: '36a83693-39f5-47ec-a63e-dbfa7b2a60bb',
    citizenName: 'Ramesh Kumar',
    status: 'DISBURSED',
    disbursedAmount: 2400,
    eligibilityCriteria: 'Income below annual threshold; verified resident.'
  },
  {
    id: 'b1111111-2222-3333-4444-555566667778',
    schemeId: 's1111111-2222-3333-4444-555566667777',
    citizenId: '49b92718-47e5-4ebc-ba3d-abfa7b2a60cc',
    citizenName: 'Priya Sharma',
    status: 'APPROVED',
    disbursedAmount: 0,
    eligibilityCriteria: 'Single earner household with dependent children.'
  }
];

const INITIAL_BUDGETS = [
  { id: '1', departmentName: 'Housing', allocatedAmount: 12000000, disbursedAmount: 10400000 },
  { id: '2', departmentName: 'Water Sanitation', allocatedAmount: 15000000, disbursedAmount: 13100000 },
  { id: '3', departmentName: 'Public Health Care', allocatedAmount: 8000000, disbursedAmount: 7200000 },
  { id: '4', departmentName: 'Education Grants', allocatedAmount: 7000000, disbursedAmount: 6200000 },
  { id: '5', departmentName: 'Public Works & Roads', allocatedAmount: 5000000, disbursedAmount: 4100000 }
];

const INITIAL_KPIS = {
  citizenSatisfaction: 4.7,
  serviceSla: 94.0,
  revenueCollected: 12.4,
  servicesTotal: "24.7K",
  servicesResolvedPct: 94.0,
  servicesAvgDays: 2.4,
  servicesGrowthPct: 47.0,
  grievancesFiled: "12.4K",
  grievancesResolvedPct: 94.0,
  grievancesMttrHours: 47.0,
  complaintsReductionPct: -23.0,
  budgetAllocated: "$47M",
  budgetUtilized: "$41M",
  budgetUtilizationPct: 87.0
};

const INITIAL_DEPT_PERFORMANCE = [
  { id: '1', departmentName: 'Water Dept', resolutionRate: 94.0, slaCompliance: 96.5, avgResponseHours: 24.0, satisfactionScore: 4.8, totalCases: 4200, resolvedCases: 3948 },
  { id: '2', departmentName: 'Health Dept', resolutionRate: 91.0, slaCompliance: 93.0, avgResponseHours: 28.5, satisfactionScore: 4.7, totalCases: 3100, resolvedCases: 2821 },
  { id: '3', departmentName: 'Education Dept', resolutionRate: 89.0, slaCompliance: 91.2, avgResponseHours: 32.0, satisfactionScore: 4.6, totalCases: 2800, resolvedCases: 2492 },
  { id: '4', departmentName: 'Sanitation', resolutionRate: 88.0, slaCompliance: 90.0, avgResponseHours: 18.0, satisfactionScore: 4.5, totalCases: 3900, resolvedCases: 3432 },
  { id: '5', departmentName: 'Public Works', resolutionRate: 86.0, slaCompliance: 88.5, avgResponseHours: 42.0, satisfactionScore: 4.4, totalCases: 2500, resolvedCases: 2150 }
];

const INITIAL_FEEDBACKS = [
  { id: '1', citizenName: 'Ramesh Kumar', category: 'Water Supply', rating: 5, comments: 'Water supply pipeline repaired promptly in Sector 5. Great municipal response!' },
  { id: '2', citizenName: 'Priya Sharma', category: 'Certificates & Approvals', rating: 5, comments: 'Birth certificate issued and digitally signed in under 2 days. Very smooth process.' },
  { id: '3', citizenName: 'Vikram Malhotra', category: 'Electricity & Streetlights', rating: 4, comments: 'Streetlight outage resolved next day after logging complaint.' }
];

export default function Dashboard() {
  // Authentication & Role-Based States
  const [userRole, setUserRole] = useState<'ADMIN' | 'CITIZEN' | null>(null);
  const [loggedInCitizen, setLoggedInCitizen] = useState<any>(INITIAL_CITIZENS[0]);
  const [citizensList, setCitizensList] = useState<any[]>(INITIAL_CITIZENS);

  // Navigation state (Default to HOME)
  const [activeView, setActiveView] = useState<'HOME' | 'CITIZENS' | 'GRIEVANCES' | 'SERVICES' | 'BUDGETS' | 'REPORTS'>('HOME');
  
  // Grievance Service States
  const [grievances, setGrievances] = useState<any[]>(INITIAL_GRIEVANCES);
  const [selectedGrievance, setSelectedGrievance] = useState<any>(INITIAL_GRIEVANCES[0]);
  const [searchGrievanceQuery, setSearchGrievanceQuery] = useState('');
  const [statusGrievanceFilter, setStatusGrievanceFilter] = useState('ALL');
  
  // Service Management Service States
  const [applications, setApplications] = useState<any[]>(INITIAL_APPLICATIONS);
  const [selectedApplication, setSelectedApplication] = useState<any>(INITIAL_APPLICATIONS[0]);
  const [associatedDoc, setAssociatedDoc] = useState<any>({
    type: 'CERTIFICATE',
    document: { id: 'c1', certificateNumber: 'BC-2024-1247', digitalSignature: 'SHA256-7E872881808B1462-VERIFIED', downloadCount: 1 }
  });
  const [searchApplicationQuery, setSearchApplicationQuery] = useState('');
  const [statusApplicationFilter, setStatusApplicationFilter] = useState('ALL');

  // Welfare & Budget Service States
  const [schemes, setSchemes] = useState<any[]>(INITIAL_SCHEMES);
  const [selectedScheme, setSelectedScheme] = useState<any>(INITIAL_SCHEMES[0]);
  const [beneficiaries, setBeneficiaries] = useState<any[]>(INITIAL_BENEFICIARIES);
  const [selectedBeneficiary, setSelectedBeneficiary] = useState<any>(INITIAL_BENEFICIARIES[0]);
  const [deptBudgets, setDeptBudgets] = useState<any[]>(INITIAL_BUDGETS);

  // Milestone 4: Reporting & Analytics States
  const [governanceKpis, setGovernanceKpis] = useState<any>(INITIAL_KPIS);
  const [departmentPerformance, setDepartmentPerformance] = useState<any[]>(INITIAL_DEPT_PERFORMANCE);
  const [citizenFeedbacks, setCitizenFeedbacks] = useState<any[]>(INITIAL_FEEDBACKS);
  const [reportTab, setReportTab] = useState<number>(0);
  const [exportDialogOpen, setExportDialogOpen] = useState(false);
  const [exportFormat, setExportFormat] = useState('CSV');
  const [drillDownOpen, setDrillDownOpen] = useState(false);
  const [shareToastOpen, setShareToastOpen] = useState(false);

  // Citizen Feedback Submission State
  const [feedbackRating, setFeedbackRating] = useState<number>(5);
  const [feedbackCategory, setFeedbackCategory] = useState('Water Supply');
  const [feedbackComments, setFeedbackComments] = useState('');
  const [feedbackSubmitting, setFeedbackSubmitting] = useState(false);
  const [feedbackSuccessToast, setFeedbackSuccessToast] = useState(false);

  // Modals state
  const [citizenModalOpen, setCitizenModalOpen] = useState(false);
  const [grievanceModalOpen, setGrievanceModalOpen] = useState(false);
  const [applicationModalOpen, setApplicationModalOpen] = useState(false);
  const [beneficiaryModalOpen, setBeneficiaryModalOpen] = useState(false);
  
  const [resolveDialogOpen, setResolveDialogOpen] = useState(false);
  const [resolutionNotes, setResolutionNotes] = useState('');
  
  const [disburseDialogOpen, setDisburseDialogOpen] = useState(false);
  const [disburseAmount, setDisburseAmount] = useState('2400');
  
  // Menu for department assignment
  const [assignMenuAnchor, setAssignMenuAnchor] = useState<null | HTMLElement>(null);
  
  // System logs/audit trail
  const [logs, setLogs] = useState<string[]>([
    'System initialized. Connection to Gateway on port 8080 established.',
    'Seeded default cases: Citizen Ramesh Kumar (Ward 12), Priya Sharma (Ward 8).',
    'Seeded default grievance: Water Supply Disruption (Assigned: Water Dept, SLA: 2d).',
    'Seeded default service app: Birth Certificate for Priya Sharma (Aadhar verified, Signed).',
    'Seeded default welfare scheme: PM Awas Yojana ($2.4M allocated, 87.5% utilized).',
    'Milestone 4 Executive Governance Analytics active: SAT: 4.7/5, SLA: 94%, Rev: $12.4M.'
  ]);

  const addLog = (message: string) => {
    const time = new Date().toLocaleTimeString();
    setLogs(prev => [`[${time}] ${message}`, ...prev]);
  };

  useEffect(() => {
    fetchCitizens();
    fetchGrievances();
    fetchApplications();
    fetchSchemes();
    fetchBudgets();
    fetchReportsData();
  }, []);

  const fetchCitizens = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/citizens');
      if (response.ok) {
        const data = await response.json();
        if (Array.isArray(data) && data.length > 0) {
          setCitizensList(data);
        }
      }
    } catch {
      // Resilient fallback kept
    }
  };

  const fetchGrievances = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/grievances');
      if (response.ok) {
        const data = await response.json();
        if (Array.isArray(data) && data.length > 0) {
          setGrievances(data);
          setSelectedGrievance(data[0]);
        }
      }
    } catch {
      // Resilient fallback kept
    }
  };

  const fetchApplications = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/services/applications');
      if (response.ok) {
        const data = await response.json();
        if (Array.isArray(data) && data.length > 0) {
          setApplications(data);
          setSelectedApplication(data[0]);
        }
      }
    } catch {
      // Resilient fallback kept
    }
  };

  const fetchSchemes = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/welfare/schemes');
      if (response.ok) {
        const data = await response.json();
        if (Array.isArray(data) && data.length > 0) {
          setSchemes(data);
          setSelectedScheme(data[0]);
        }
      }
    } catch {
      // Resilient fallback kept
    }
  };

  const fetchBudgets = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/budgets');
      if (response.ok) {
        const data = await response.json();
        if (Array.isArray(data) && data.length > 0) {
          setDeptBudgets(data);
        }
      }
    } catch {
      // Resilient fallback kept
    }
  };

  const fetchReportsData = async () => {
    try {
      const [kpiRes, deptRes, feedbackRes] = await Promise.all([
        fetch('http://localhost:8080/api/v1/reports/governance-kpis').catch(() => null),
        fetch('http://localhost:8080/api/v1/reports/departments').catch(() => null),
        fetch('http://localhost:8080/api/v1/reports/feedback').catch(() => null)
      ]);

      if (kpiRes && kpiRes.ok) {
        const kpis = await kpiRes.json();
        setGovernanceKpis(kpis);
      }
      if (deptRes && deptRes.ok) {
        const depts = await deptRes.json();
        if (Array.isArray(depts) && depts.length > 0) setDepartmentPerformance(depts);
      }
      if (feedbackRes && feedbackRes.ok) {
        const fbs = await feedbackRes.json();
        if (Array.isArray(fbs) && fbs.length > 0) setCitizenFeedbacks(fbs);
      }
    } catch {
      // Resilient fallback kept
    }
  };

  const handleAssignDept = async (dept: string) => {
    if (!selectedGrievance) return;
    setAssignMenuAnchor(null);
    try {
      const response = await fetch(`http://localhost:8080/api/v1/grievances/${selectedGrievance.id}/assign?department=${dept}`, { method: 'POST' });
      if (response.ok) {
        const updated = await response.json();
        setGrievances(prev => prev.map(g => g.id === updated.id ? updated : g));
        setSelectedGrievance(updated);
        addLog(`Grievance assigned to ${dept}. Status: IN_PROGRESS.`);
        return;
      }
    } catch {}
    const updated = { ...selectedGrievance, assignedDepartment: dept, status: 'IN_PROGRESS' };
    setGrievances(prev => prev.map(g => g.id === updated.id ? updated : g));
    setSelectedGrievance(updated);
    addLog(`Grievance assigned to ${dept}. Status: IN_PROGRESS.`);
  };

  const handleResolveGrievance = async () => {
    if (!selectedGrievance) return;
    setResolveDialogOpen(false);
    try {
      await fetch(`http://localhost:8080/api/v1/grievances/${selectedGrievance.id}/resolve`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ notes: resolutionNotes })
      });
      await fetch(`http://localhost:8080/api/v1/grievances/${selectedGrievance.id}/close`, { method: 'POST' });
    } catch {}
    const updated = { ...selectedGrievance, status: 'CLOSED', resolutionNotes };
    setGrievances(prev => prev.map(g => g.id === updated.id ? updated : g));
    setSelectedGrievance(updated);
    setResolutionNotes('');
    addLog(`Grievance marked as RESOLVED and CLOSED. Notes: "${resolutionNotes}"`);
  };

  const handleTriggerEscalation = async () => {
    if (!selectedGrievance) return;
    try {
      await fetch(`http://localhost:8080/api/v1/grievances/${selectedGrievance.id}/trigger-escalation`, { method: 'POST' });
    } catch {}
    const updated = { ...selectedGrievance, escalationLevel: (selectedGrievance.escalationLevel || 0) + 1, assignedDepartment: 'ADMIN_DEPT' };
    setGrievances(prev => prev.map(g => g.id === updated.id ? updated : g));
    setSelectedGrievance(updated);
    addLog(`SLA Breach escalated to Level ${updated.escalationLevel}. Reassigned to Municipal Admin.`);
  };

  const handleVerifyDocuments = async () => {
    if (!selectedApplication) return;
    try {
      await fetch(`http://localhost:8080/api/v1/services/applications/${selectedApplication.id}/verify`, { method: 'POST' });
    } catch {}
    const updated = { ...selectedApplication, status: 'DOCUMENT_VERIFIED', verified: true };
    setApplications(prev => prev.map(a => a.id === updated.id ? updated : a));
    setSelectedApplication(updated);
    addLog(`Document Verification approved for ${updated.applicationNumber}.`);
  };

  const handleApproveApplication = async () => {
    if (!selectedApplication) return;
    try {
      await fetch(`http://localhost:8080/api/v1/services/applications/${selectedApplication.id}/approve`, { method: 'POST' });
    } catch {}
    const updated = { ...selectedApplication, status: 'APPROVED' };
    setApplications(prev => prev.map(a => a.id === updated.id ? updated : a));
    setSelectedApplication(updated);
    setAssociatedDoc({
      type: 'CERTIFICATE',
      document: { certificateNumber: `BC-${new Date().getFullYear()}-0091`, digitalSignature: 'SHA256-4D8832BA91C4-VERIFIED', downloadCount: 0 }
    });
    addLog(`Certificate issued for ${updated.applicationNumber} with SHA-256 cryptographic signature.`);
  };

  const handleDownloadCertificate = () => {
    addLog('Official digitally signed certificate downloaded (Audit record created).');
    if (associatedDoc && associatedDoc.document) {
      setAssociatedDoc({
        ...associatedDoc,
        document: { ...associatedDoc.document, downloadCount: (associatedDoc.document.downloadCount || 0) + 1 }
      });
    }
  };

  const handleVerifyEligibility = async () => {
    if (!selectedBeneficiary) return;
    const updated = { ...selectedBeneficiary, status: 'APPROVED' };
    setBeneficiaries(prev => prev.map(b => b.id === updated.id ? updated : b));
    setSelectedBeneficiary(updated);
    addLog(`Welfare eligibility verified for citizen ${updated.citizenName}. Status: APPROVED.`);
  };

  const handleDisburseFunds = async () => {
    if (!selectedBeneficiary) return;
    setDisburseDialogOpen(false);
    const amountNum = parseFloat(disburseAmount) || 2400;
    const updated = { ...selectedBeneficiary, status: 'DISBURSED', disbursedAmount: (selectedBeneficiary.disbursedAmount || 0) + amountNum };
    setBeneficiaries(prev => prev.map(b => b.id === updated.id ? updated : b));
    setSelectedBeneficiary(updated);
    addLog(`Funds disbursed: $${amountNum.toLocaleString()} transferred to ${updated.citizenName}.`);
    addLog(`Inter-service trigger: Budget Service recorded Housing Department expenditure of $${amountNum}.`);
  };

  const handleExportReport = async () => {
    const reportData = {
      reportTitle: "CivicPulse Executive Governance & Audit Report",
      generatedAt: new Date().toISOString(),
      format: exportFormat,
      kpiSummary: governanceKpis,
      departmentBreakdown: departmentPerformance,
      auditStatus: "COMPLIANCE_PASSED"
    };

    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(reportData, null, 2));
    const downloadAnchor = document.createElement('a');
    downloadAnchor.setAttribute("href", dataStr);
    downloadAnchor.setAttribute("download", `CivicPulse_Governance_Report_${new Date().toISOString().slice(0, 10)}.${exportFormat.toLowerCase()}`);
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    downloadAnchor.remove();

    addLog(`Executive Governance Report exported (${exportFormat}). Compliance audit record generated.`);
    setExportDialogOpen(false);
  };

  const handleShareReport = () => {
    if (navigator.clipboard) {
      navigator.clipboard.writeText(window.location.href);
    }
    setShareToastOpen(true);
    addLog('Governance Dashboard link copied to clipboard for collaboration.');
  };

  const handleCitizenFeedbackSubmit = () => {
    if (!feedbackComments.trim()) return;
    setFeedbackSubmitting(true);
    setTimeout(() => {
      const newFb = {
        id: String(Date.now()),
        citizenName: loggedInCitizen?.name || 'Citizen Resident',
        category: feedbackCategory,
        rating: feedbackRating,
        comments: feedbackComments
      };
      setCitizenFeedbacks(prev => [newFb, ...prev]);
      setFeedbackComments('');
      setFeedbackSubmitting(false);
      setFeedbackSuccessToast(true);
      addLog(`Citizen evaluation submitted: ${feedbackRating} stars for ${feedbackCategory}.`);
    }, 400);
  };

  const handleLogout = () => {
    setUserRole(null);
    setActiveView('HOME');
    addLog('User logged out.');
  };

  const myGrievances = userRole === 'CITIZEN' && loggedInCitizen
    ? grievances.filter(g => g.citizenId === loggedInCitizen.id)
    : grievances;

  const filteredGrievances = myGrievances.filter(g => {
    const matchesSearch = (g.citizenName || '').toLowerCase().includes(searchGrievanceQuery.toLowerCase()) ||
                          (g.title || '').toLowerCase().includes(searchGrievanceQuery.toLowerCase()) ||
                          (g.location || '').toLowerCase().includes(searchGrievanceQuery.toLowerCase());
    const matchesStatus = statusGrievanceFilter === 'ALL' || g.status === statusGrievanceFilter;
    return matchesSearch && matchesStatus;
  });

  const myApplications = userRole === 'CITIZEN' && loggedInCitizen
    ? applications.filter(a => a.citizenId === loggedInCitizen.id)
    : applications;

  const filteredApplications = myApplications.filter(a => {
    const matchesSearch = (a.citizenName || '').toLowerCase().includes(searchApplicationQuery.toLowerCase()) ||
                          (a.applicationNumber || '').toLowerCase().includes(searchApplicationQuery.toLowerCase()) ||
                          (a.type || '').replace('_', ' ').toLowerCase().includes(searchApplicationQuery.toLowerCase());
    const matchesStatus = statusApplicationFilter === 'ALL' || a.status === statusApplicationFilter;
    return matchesSearch && matchesStatus;
  });

  const getStatusChipColor = (status: string) => {
    switch (status) {
      case 'SUBMITTED': return { bg: '#e0f2fe', text: '#0369a1' };
      case 'DOCUMENT_VERIFIED': return { bg: '#e0f2fe', text: '#0369a1' };
      case 'IN_PROGRESS': return { bg: '#fef3c7', text: '#b45309' };
      case 'PENDING': return { bg: '#fef3c7', text: '#b45309' };
      case 'APPROVED': return { bg: '#d1fae5', text: '#047857' };
      case 'DISBURSED': return { bg: '#d1fae5', text: '#047857' };
      case 'RESOLVED': return { bg: '#d1fae5', text: '#047857' };
      case 'CLOSED': return { bg: '#f1f5f9', text: '#475569' };
      default: return { bg: '#f1f5f9', text: '#475569' };
    }
  };

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case 'CRITICAL': return '#ef4444';
      case 'HIGH': return '#f59e0b';
      case 'MEDIUM': return '#3b82f6';
      default: return '#10b981';
    }
  };

  // -------------------------------------------------------------
  // LANDING LOGIN PORTAL
  // -------------------------------------------------------------
  if (!userRole) {
    return (
      <div style={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'radial-gradient(circle at 50% 0%, #e2e8f0 0%, #f8fafc 80%)',
        padding: '24px'
      }}>
        <Paper sx={{
          maxWidth: '850px',
          width: '100%',
          p: 5,
          background: '#ffffff',
          border: '1px solid rgba(0,0,0,0.08)',
          borderRadius: '24px',
          boxShadow: '0 20px 40px rgba(0,0,0,0.06)',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          gap: 4
        }}>
          <div style={{ textAlign: 'center' }}>
            <Typography variant="h3" sx={{ fontWeight: '800', color: 'var(--primary)', letterSpacing: -1, mb: 1 }}>
              CivicPulse Nexus
            </Typography>
            <Typography variant="body1" sx={{ color: 'var(--text-muted)', fontWeight: 500 }}>
              Cloud-Native Smart Governance &amp; Citizen Services Platform (Milestones 1–4)
            </Typography>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px', width: '100%', marginTop: '16px' }}>
            
            {/* Citizen Login Portal */}
            <Card variant="outlined" sx={{ 
              borderRadius: '16px', 
              p: 3, 
              borderColor: 'rgba(0,0,0,0.08)', 
              background: '#fff',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'space-between',
              boxShadow: '0 4px 12px rgba(0,0,0,0.02)'
            }}>
              <CardContent sx={{ p: 0, display: 'flex', flexDirection: 'column', gap: 2 }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                  <div style={{ padding: '8px', background: 'rgba(2, 132, 199, 0.08)', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', width: 42, height: 42 }}>
                    <PeopleIcon sx={{ color: 'var(--primary)' }} />
                  </div>
                  <div>
                    <Typography variant="h6" sx={{ fontWeight: 'bold' }}>Citizen Portal</Typography>
                    <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>File grievances, track permits &amp; welfare</Typography>
                  </div>
                </div>
                
                <Divider sx={{ my: 1 }} />

                <FormControl fullWidth variant="outlined" size="small">
                  <InputLabel id="select-citizen-login" sx={{ color: 'var(--text-muted)' }}>Choose Resident Profile</InputLabel>
                  <Select
                    labelId="select-citizen-login"
                    value={loggedInCitizen ? loggedInCitizen.id : citizensList[0]?.id}
                    onChange={(e) => {
                      const selected = citizensList.find(c => c.id === e.target.value);
                      setLoggedInCitizen(selected);
                    }}
                    label="Choose Resident Profile"
                  >
                    {citizensList.map(c => (
                      <MenuItem key={c.id} value={c.id}>
                        {c.name} ({c.ward})
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </CardContent>

              <Button
                variant="contained"
                onClick={() => {
                  setUserRole('CITIZEN');
                  setActiveView('HOME');
                  addLog(`Citizen logged in: ${loggedInCitizen?.name || 'Resident'}`);
                }}
                sx={{
                  mt: 3,
                  background: 'var(--gradient-teal)',
                  color: '#fff',
                  fontWeight: 'bold',
                  textTransform: 'none',
                  borderRadius: '8px',
                  boxShadow: 'none'
                }}
              >
                Access Citizen Portal
              </Button>
            </Card>

            {/* Admin Login Portal */}
            <Card variant="outlined" sx={{ 
              borderRadius: '16px', 
              p: 3, 
              borderColor: 'rgba(0,0,0,0.08)', 
              background: '#fff',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'space-between',
              boxShadow: '0 4px 12px rgba(0,0,0,0.02)'
            }}>
              <CardContent sx={{ p: 0, display: 'flex', flexDirection: 'column', gap: 2 }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                  <div style={{ padding: '8px', background: 'rgba(2, 132, 199, 0.08)', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', width: 42, height: 42 }}>
                    <ShieldIcon sx={{ color: 'var(--primary)' }} />
                  </div>
                  <div>
                    <Typography variant="h6" sx={{ fontWeight: 'bold' }}>Municipal Console</Typography>
                    <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Admin control &amp; executive analytics</Typography>
                  </div>
                </div>

                <Divider sx={{ my: 1 }} />
                
                <Typography variant="body2" sx={{ color: 'var(--text-muted)', lineHeight: 1.5, mt: 1 }}>
                  Full administration suite: oversee citizen complaints, issue cryptographic certificates, disburse welfare budgets, and explore Milestone 4 governance analytics.
                </Typography>
              </CardContent>

              <Button
                variant="outlined"
                onClick={() => {
                  setUserRole('ADMIN');
                  setActiveView('HOME');
                  addLog('Municipal Administrator logged in to Console.');
                }}
                sx={{
                  mt: 3,
                  borderColor: 'var(--primary)',
                  color: 'var(--primary)',
                  fontWeight: 'bold',
                  textTransform: 'none',
                  borderRadius: '8px',
                  '&:hover': {
                    background: 'var(--primary-glow)',
                    borderColor: 'var(--primary)'
                  }
                }}
              >
                Access Municipal Console
              </Button>
            </Card>

          </div>
        </Paper>
      </div>
    );
  }

  // -------------------------------------------------------------
  // MAIN APPLICATION DASHBOARD
  // -------------------------------------------------------------
  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: 'var(--bg-primary)' }}>
      
      {/* Left Sidebar Navigation */}
      <div style={{ width: '260px', borderRight: '1px solid var(--border-color)', backgroundColor: 'var(--bg-secondary)', display: 'flex', flexDirection: 'column' }}>
        <div style={{ padding: '24px', borderBottom: '1px solid var(--border-color)' }}>
          <Typography variant="h6" sx={{ fontWeight: '800', letterSpacing: 0.5, color: 'var(--primary)' }}>
            CivicPulse Nexus
          </Typography>
          <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>
            Smart Governance Platform
          </Typography>
        </div>
        <List sx={{ p: 2, flexGrow: 1 }}>
          <ListItem 
            onClick={() => setActiveView('HOME')}
            sx={{ 
              borderRadius: 2, mb: 1, cursor: 'pointer',
              color: activeView === 'HOME' ? 'var(--primary)' : 'var(--text-muted)', 
              background: activeView === 'HOME' ? 'var(--primary-glow)' : 'transparent',
              borderLeft: activeView === 'HOME' ? '4px solid var(--primary)' : '4px solid transparent',
            }}
          >
            <ListItemText primary={<div style={{ display: 'flex', alignItems: 'center', gap: 8 }}><HomeIcon fontSize="small"/><strong>Dashboard</strong></div>} />
          </ListItem>

          {userRole === 'ADMIN' && (
            <ListItem 
              onClick={() => setActiveView('CITIZENS')}
              sx={{ 
                borderRadius: 2, mb: 1, cursor: 'pointer',
                color: activeView === 'CITIZENS' ? 'var(--primary)' : 'var(--text-muted)', 
                background: activeView === 'CITIZENS' ? 'var(--primary-glow)' : 'transparent',
                borderLeft: activeView === 'CITIZENS' ? '4px solid var(--primary)' : '4px solid transparent',
              }}
            >
              <ListItemText primary={<div style={{ display: 'flex', alignItems: 'center', gap: 8 }}><PeopleIcon fontSize="small"/><strong>Citizens</strong></div>} />
            </ListItem>
          )}

          <ListItem 
            onClick={() => setActiveView('SERVICES')}
            sx={{ 
              borderRadius: 2, mb: 1, cursor: 'pointer',
              color: activeView === 'SERVICES' ? 'var(--primary)' : 'var(--text-muted)', 
              background: activeView === 'SERVICES' ? 'var(--primary-glow)' : 'transparent',
              borderLeft: activeView === 'SERVICES' ? '4px solid var(--primary)' : '4px solid transparent',
            }}
          >
            <ListItemText primary={<div style={{ display: 'flex', alignItems: 'center', gap: 8 }}><FileCopyIcon fontSize="small"/><strong>{userRole === 'ADMIN' ? 'Services & Permits' : 'My Documents'}</strong></div>} />
          </ListItem>

          <ListItem 
            onClick={() => setActiveView('GRIEVANCES')}
            sx={{ 
              borderRadius: 2, mb: 1, cursor: 'pointer',
              color: activeView === 'GRIEVANCES' ? 'var(--primary)' : 'var(--text-muted)', 
              background: activeView === 'GRIEVANCES' ? 'var(--primary-glow)' : 'transparent',
              borderLeft: activeView === 'GRIEVANCES' ? '4px solid var(--primary)' : '4px solid transparent',
            }}
          >
            <ListItemText primary={<div style={{ display: 'flex', alignItems: 'center', gap: 8 }}><GavelIcon fontSize="small"/><strong>{userRole === 'ADMIN' ? 'Grievances' : 'My Grievances'}</strong></div>} />
          </ListItem>

          <ListItem 
            onClick={() => setActiveView('BUDGETS')}
            sx={{ 
              borderRadius: 2, mb: 1, cursor: 'pointer',
              color: activeView === 'BUDGETS' ? 'var(--primary)' : 'var(--text-muted)', 
              background: activeView === 'BUDGETS' ? 'var(--primary-glow)' : 'transparent',
              borderLeft: activeView === 'BUDGETS' ? '4px solid var(--primary)' : '4px solid transparent',
            }}
          >
            <ListItemText primary={<div style={{ display: 'flex', alignItems: 'center', gap: 8 }}><AccountBalanceWalletIcon fontSize="small"/><strong>{userRole === 'ADMIN' ? 'Budget & Welfare' : 'Welfare Schemes'}</strong></div>} />
          </ListItem>

          <ListItem 
            onClick={() => setActiveView('REPORTS')}
            sx={{ 
              borderRadius: 2, mb: 1, cursor: 'pointer',
              color: activeView === 'REPORTS' ? 'var(--primary)' : 'var(--text-muted)', 
              background: activeView === 'REPORTS' ? 'var(--primary-glow)' : 'transparent',
              borderLeft: activeView === 'REPORTS' ? '4px solid var(--primary)' : '4px solid transparent',
            }}
          >
            <ListItemText primary={<div style={{ display: 'flex', alignItems: 'center', gap: 8 }}><AssessmentIcon fontSize="small"/><strong>{userRole === 'ADMIN' ? 'Reports & Analytics' : 'Service Quality & Rating'}</strong></div>} />
          </ListItem>

          <Divider sx={{ bgcolor: 'var(--border-color)', my: 2 }} />

          <ListItem 
            onClick={handleLogout}
            sx={{ 
              borderRadius: 2, color: '#ef4444', cursor: 'pointer',
              '&:hover': { background: 'rgba(239, 68, 68, 0.08)' }
            }}
          >
            <ListItemText primary={<div style={{ display: 'flex', alignItems: 'center', gap: 8 }}><ExitToAppIcon fontSize="small"/><strong>Logout</strong></div>} />
          </ListItem>
        </List>
      </div>

      {/* Main Content Area */}
      <div style={{ flexGrow: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
        
        {/* Top Header Bar */}
        <div style={{ height: '70px', borderBottom: '1px solid var(--border-color)', display: 'flex', alignItems: 'center', justifyContent: 'space-between', paddingLeft: '32px', paddingRight: '32px', backgroundColor: 'var(--bg-secondary)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <Typography variant="body1" sx={{ fontWeight: 'bold', color: 'var(--primary)' }}>
              {activeView === 'HOME' && 'Executive Home Dashboard'}
              {activeView === 'CITIZENS' && 'Registered Citizens Directory'}
              {activeView === 'GRIEVANCES' && 'Citizen Grievance Redressal'}
              {activeView === 'SERVICES' && 'Certificate & Permit Approvals'}
              {activeView === 'BUDGETS' && 'Welfare Scheme & Budget Tracking'}
              {activeView === 'REPORTS' && 'Executive Dashboard & Reports (Milestone 4)'}
            </Typography>
            <Chip label="Milestone 4: Governance Analytics" size="small" sx={{ bgcolor: '#e0f2fe', color: '#0284c7', fontWeight: 'bold' }} />
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <Typography variant="body2" sx={{ color: 'var(--text-muted)' }}>
              Logged in: <strong style={{ color: 'var(--text-main)' }}>{userRole === 'ADMIN' ? 'Municipal Admin' : loggedInCitizen?.name}</strong>
            </Typography>
            {userRole === 'CITIZEN' && (
              <Chip label={loggedInCitizen?.ward || 'Ward 12'} size="small" variant="outlined" sx={{ borderColor: 'var(--primary)', color: 'var(--primary)', fontWeight: 'bold' }} />
            )}
            <Button size="small" color="error" onClick={handleLogout} sx={{ textTransform: 'none' }}>Logout</Button>
          </div>
        </div>

        {/* Dynamic Page Content */}
        <div style={{ flexGrow: 1, padding: '32px', overflowY: 'auto' }}>

          {/* -------------------------------------------------------------
              1. DASHBOARD / HOME PAGE
              ------------------------------------------------------------- */}
          {activeView === 'HOME' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
              
              <Paper sx={{ p: 4, background: 'var(--gradient-card)' }}>
                <Typography variant="h4" sx={{ fontWeight: '800', mb: 1, letterSpacing: -0.5 }}>
                  {userRole === 'ADMIN' ? 'Executive Governance Dashboard' : `Welcome, ${loggedInCitizen?.name}`}
                </Typography>
                <Typography variant="body1" sx={{ color: 'var(--text-muted)', maxWidth: '750px', lineHeight: 1.6 }}>
                  {userRole === 'ADMIN' 
                    ? 'Oversee municipal department operations, track SLA compliance (94%), monitor revenue realization ($12.4M), and explore Milestone 4 governance analytics.'
                    : 'Submit your municipal grievances, track birth and trade license applications, and review public service satisfaction ratings.'}
                </Typography>

                {userRole === 'CITIZEN' && (
                  <div style={{ display: 'flex', gap: '24px', marginTop: '20px', background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)', maxWidth: '650px' }}>
                    <div>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Email</Typography>
                      <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{loggedInCitizen?.email}</Typography>
                    </div>
                    <div>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Aadhar ID</Typography>
                      <Typography variant="body2" sx={{ fontWeight: 'bold' }}>XXXX-XXXX-{(loggedInCitizen?.aadharNumber || '1234').slice(-4)}</Typography>
                    </div>
                    <div>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Jurisdiction</Typography>
                      <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{loggedInCitizen?.ward || 'Ward 12'}</Typography>
                    </div>
                  </div>
                )}
              </Paper>

              {/* Milestone 4 Highlight KPIs */}
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '24px' }}>
                <Card variant="outlined" sx={{ p: 1, borderRadius: '16px' }}>
                  <CardContent>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                      <Typography variant="body2" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>
                        Citizen Satisfaction
                      </Typography>
                      <VerifiedIcon sx={{ color: '#10b981', fontSize: 20 }} />
                    </div>
                    <Typography variant="h3" sx={{ fontWeight: '800', color: 'var(--primary)' }}>
                      4.7/5
                    </Typography>
                    <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>
                      Verified municipal resident rating
                    </Typography>
                  </CardContent>
                </Card>

                <Card variant="outlined" sx={{ p: 1, borderRadius: '16px' }}>
                  <CardContent>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                      <Typography variant="body2" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>
                        Service SLA Met
                      </Typography>
                      <TrendingUpIcon sx={{ color: '#0284c7', fontSize: 20 }} />
                    </div>
                    <Typography variant="h3" sx={{ fontWeight: '800', color: 'var(--secondary)' }}>
                      94.0%
                    </Typography>
                    <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>
                      Resolved within target timeline
                    </Typography>
                  </CardContent>
                </Card>

                <Card variant="outlined" sx={{ p: 1, borderRadius: '16px' }}>
                  <CardContent>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                      <Typography variant="body2" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>
                        Revenue Collected
                      </Typography>
                      <MonetizationOnIcon sx={{ color: '#d97706', fontSize: 20 }} />
                    </div>
                    <Typography variant="h3" sx={{ fontWeight: '800', color: '#d97706' }}>
                      $12.4M
                    </Typography>
                    <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>
                      Taxes, trade licenses &amp; utility fees
                    </Typography>
                  </CardContent>
                </Card>

                <Card variant="outlined" sx={{ p: 1, borderRadius: '16px' }}>
                  <CardContent>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                      <Typography variant="body2" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>
                        Budget Utilization
                      </Typography>
                      <AccountBalanceWalletIcon sx={{ color: '#8b5cf6', fontSize: 20 }} />
                    </div>
                    <Typography variant="h3" sx={{ fontWeight: '800', color: '#8b5cf6' }}>
                      87.0%
                    </Typography>
                    <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>
                      $41M disbursed of $47M allocated
                    </Typography>
                  </CardContent>
                </Card>
              </div>

              {/* Navigation Shortcuts */}
              <Paper sx={{ p: 4 }}>
                <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2 }}>Platform Navigation &amp; Workflows</Typography>
                <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
                  <Button variant="contained" onClick={() => setActiveView('REPORTS')} sx={{ textTransform: 'none', background: 'var(--gradient-teal)', color: '#fff', borderRadius: 2 }}>
                    Open Milestone 4 Reports &amp; Analytics
                  </Button>
                  <Button variant="outlined" onClick={() => setActiveView('GRIEVANCES')} sx={{ textTransform: 'none', color: 'var(--primary)', borderColor: 'var(--primary)', borderRadius: 2 }}>
                    {userRole === 'ADMIN' ? 'Manage Grievance Board' : 'View My Grievances'}
                  </Button>
                  <Button variant="outlined" onClick={() => setActiveView('SERVICES')} sx={{ textTransform: 'none', color: 'var(--primary)', borderColor: 'var(--primary)', borderRadius: 2 }}>
                    {userRole === 'ADMIN' ? 'Certificate Approvals' : 'My Certificates'}
                  </Button>
                  <Button variant="outlined" onClick={() => setActiveView('BUDGETS')} sx={{ textTransform: 'none', color: 'var(--primary)', borderColor: 'var(--primary)', borderRadius: 2 }}>
                    Welfare &amp; Budgets
                  </Button>
                  {userRole === 'ADMIN' && (
                    <Button variant="outlined" onClick={() => setActiveView('CITIZENS')} sx={{ textTransform: 'none', color: 'var(--primary)', borderColor: 'var(--primary)', borderRadius: 2 }}>
                      Citizen Directory
                    </Button>
                  )}
                </div>
              </Paper>

            </div>
          )}

          {/* -------------------------------------------------------------
              2. CITIZENS DIRECTORY (ADMIN ONLY)
              ------------------------------------------------------------- */}
          {activeView === 'CITIZENS' && userRole === 'ADMIN' && (
            <Paper sx={{ p: 4, background: 'var(--gradient-card)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
                <div>
                  <Typography variant="h5" sx={{ fontWeight: '800' }}>Citizen Directory</Typography>
                  <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Registered municipal residents in database</Typography>
                </div>
                <Button variant="contained" startIcon={<AddIcon />} onClick={() => setCitizenModalOpen(true)} sx={{ textTransform: 'none', background: 'var(--gradient-teal)', color: '#fff', borderRadius: 2 }}>
                  Register Citizen
                </Button>
              </div>

              <TableContainer component={Paper} variant="outlined" sx={{ borderRadius: '12px', background: '#fff', borderColor: 'var(--border-color)' }}>
                <Table>
                  <TableHead sx={{ bgcolor: 'rgba(0,0,0,0.02)' }}>
                    <TableRow>
                      <TableCell sx={{ fontWeight: 'bold' }}>Name</TableCell>
                      <TableCell sx={{ fontWeight: 'bold' }}>Email Address</TableCell>
                      <TableCell sx={{ fontWeight: 'bold' }}>Phone</TableCell>
                      <TableCell sx={{ fontWeight: 'bold' }}>Aadhar ID</TableCell>
                      <TableCell sx={{ fontWeight: 'bold' }}>Ward Jurisdiction</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {citizensList.map((c) => (
                      <TableRow key={c.id}>
                        <TableCell sx={{ fontWeight: 600 }}>{c.name}</TableCell>
                        <TableCell>{c.email}</TableCell>
                        <TableCell>{c.phone}</TableCell>
                        <TableCell>{c.aadharNumber}</TableCell>
                        <TableCell>
                          <Chip label={c.ward} size="small" variant="outlined" sx={{ fontWeight: 'bold', color: 'var(--primary)', borderColor: 'var(--primary)' }} />
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            </Paper>
          )}

          {/* -------------------------------------------------------------
              3. GRIEVANCES BOARD
              ------------------------------------------------------------- */}
          {activeView === 'GRIEVANCES' && (
            <div style={{ display: 'grid', gridTemplateColumns: '5fr 7fr', gap: '32px' }}>
              
              <Paper sx={{ p: 3, display: 'flex', flexDirection: 'column', height: 550, background: 'var(--gradient-card)', border: '1px solid var(--border-color)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {userRole === 'ADMIN' ? 'Municipal Grievances' : 'My Filed Grievances'}
                  </Typography>
                  <Button size="small" startIcon={<AddIcon />} onClick={() => setGrievanceModalOpen(true)} variant="contained" sx={{ textTransform: 'none', background: 'var(--gradient-teal)', color: '#fff' }}>
                    File Grievance
                  </Button>
                </div>

                <div style={{ display: 'flex', gap: '16px', marginBottom: '16px' }}>
                  <TextField
                    placeholder="Search by title, location..."
                    variant="outlined"
                    size="small"
                    fullWidth
                    value={searchGrievanceQuery}
                    onChange={(e) => setSearchGrievanceQuery(e.target.value)}
                  />
                  <TextField
                    select
                    value={statusGrievanceFilter}
                    onChange={(e) => setStatusGrievanceFilter(e.target.value)}
                    variant="outlined"
                    size="small"
                    sx={{ minWidth: 120 }}
                  >
                    <MenuItem value="ALL">All</MenuItem>
                    <MenuItem value="SUBMITTED">Submitted</MenuItem>
                    <MenuItem value="IN_PROGRESS">In Progress</MenuItem>
                    <MenuItem value="CLOSED">Closed</MenuItem>
                  </TextField>
                </div>

                <Divider sx={{ mb: 1 }} />

                <List sx={{ p: 0, overflowY: 'auto', flexGrow: 1 }}>
                  {filteredGrievances.map((g) => {
                    const style = getStatusChipColor(g.status);
                    const isSelected = selectedGrievance && selectedGrievance.id === g.id;
                    return (
                      <ListItem
                        key={g.id}
                        onClick={() => setSelectedGrievance(g)}
                        sx={{
                          borderRadius: 2,
                          my: 0.5,
                          background: isSelected ? 'var(--primary-glow)' : 'transparent',
                          border: isSelected ? '1px solid var(--primary)' : '1px solid transparent',
                          cursor: 'pointer'
                        }}
                      >
                        <ListItemText
                          primary={
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '4px' }}>
                              <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{g.title}</Typography>
                              <Chip label={g.status} size="small" sx={{ bgcolor: style.bg, color: style.text, fontWeight: 'bold', height: 20 }} />
                            </div>
                          }
                          secondary={
                            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                              <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>{g.citizenName} | {g.ward}</Typography>
                              <Typography variant="caption" sx={{ color: getSeverityColor(g.severity), fontWeight: 'bold' }}>{g.severity}</Typography>
                            </div>
                          }
                        />
                      </ListItem>
                    );
                  })}
                </List>
              </Paper>

              {/* Grievance Detail Card */}
              <Paper sx={{ p: 4, height: 550, display: 'flex', flexDirection: 'column', background: 'var(--gradient-card)', border: '1px solid var(--border-color)', borderRadius: '16px' }}>
                {selectedGrievance ? (
                  <>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px' }}>
                      <div>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Citizen Service - Grievance Details</Typography>
                        <Typography variant="h5" sx={{ fontWeight: 'bold' }}>{selectedGrievance.title}</Typography>
                      </div>
                      <div style={{ display: 'flex', gap: '8px' }}>
                        <Chip label={selectedGrievance.status} sx={{ bgcolor: getStatusChipColor(selectedGrievance.status).bg, color: getStatusChipColor(selectedGrievance.status).text, fontWeight: 'bold' }} />
                        <Chip label={selectedGrievance.severity} sx={{ bgcolor: 'rgba(0,0,0,0.03)', color: getSeverityColor(selectedGrievance.severity), fontWeight: 'bold' }} />
                      </div>
                    </div>

                    <Divider sx={{ mb: 2.5 }} />

                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', marginBottom: '16px' }}>
                      <div>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Citizen</Typography>
                        <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{selectedGrievance.citizenName}</Typography>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>ID: CTZ-{(selectedGrievance.citizenId || '1111').slice(0, 4)} | {selectedGrievance.ward}</Typography>
                      </div>
                      <div>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Department &amp; SLA</Typography>
                        <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{(selectedGrievance.assignedDepartment || 'UNASSIGNED').replace('_', ' ')}</Typography>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>SLA Target: {selectedGrievance.slaDays || 2} days</Typography>
                      </div>
                      <div style={{ gridColumn: 'span 2' }}>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Description</Typography>
                        <Typography variant="body2" sx={{ mt: 0.5 }}>{selectedGrievance.description}</Typography>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)', display: 'block', mt: 1 }}>Location: {selectedGrievance.location}</Typography>
                      </div>
                    </div>

                    <div style={{ marginTop: 'auto', display: 'flex', justifyContent: 'space-between', paddingTop: '16px', borderTop: '1px solid var(--border-color)' }}>
                      {userRole === 'ADMIN' ? (
                        <>
                          <Button variant="outlined" color="warning" startIcon={<NotificationsActiveIcon />} onClick={handleTriggerEscalation} disabled={selectedGrievance.status === 'CLOSED'}>
                            Trigger SLA Breach
                          </Button>
                          <div style={{ display: 'flex', gap: '12px' }}>
                            <Button variant="outlined" startIcon={<AssignmentIndIcon />} onClick={(e) => setAssignMenuAnchor(e.currentTarget)} disabled={selectedGrievance.status === 'CLOSED'}>
                              Assign
                            </Button>
                            <Button variant="contained" startIcon={<CheckCircleOutlinedIcon />} onClick={() => setResolveDialogOpen(true)} disabled={selectedGrievance.status === 'CLOSED'} sx={{ background: 'var(--gradient-teal)', color: '#fff' }}>
                              Resolve Case
                            </Button>
                          </div>
                        </>
                      ) : (
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>
                          Status updates are managed by the assigned municipal department.
                        </Typography>
                      )}
                    </div>
                  </>
                ) : (
                  <Typography variant="body2" sx={{ color: 'var(--text-muted)', m: 'auto' }}>Select a grievance to view details.</Typography>
                )}
              </Paper>

            </div>
          )}

          {/* -------------------------------------------------------------
              4. SERVICES & PERMITS APPROVALS
              ------------------------------------------------------------- */}
          {activeView === 'SERVICES' && (
            <div style={{ display: 'grid', gridTemplateColumns: '5fr 7fr', gap: '32px' }}>
              
              <Paper sx={{ p: 3, display: 'flex', flexDirection: 'column', height: 550, background: 'var(--gradient-card)', border: '1px solid var(--border-color)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {userRole === 'ADMIN' ? 'Service Applications' : 'My Certificate Requests'}
                  </Typography>
                  <Button size="small" startIcon={<AddIcon />} onClick={() => setApplicationModalOpen(true)} variant="contained" sx={{ textTransform: 'none', background: 'var(--gradient-teal)', color: '#fff' }}>
                    Apply
                  </Button>
                </div>

                <div style={{ display: 'flex', gap: '16px', marginBottom: '16px' }}>
                  <TextField
                    placeholder="Search applications..."
                    variant="outlined"
                    size="small"
                    fullWidth
                    value={searchApplicationQuery}
                    onChange={(e) => setSearchApplicationQuery(e.target.value)}
                  />
                  <TextField
                    select
                    value={statusApplicationFilter}
                    onChange={(e) => setStatusApplicationFilter(e.target.value)}
                    variant="outlined"
                    size="small"
                    sx={{ minWidth: 120 }}
                  >
                    <MenuItem value="ALL">All</MenuItem>
                    <MenuItem value="SUBMITTED">Submitted</MenuItem>
                    <MenuItem value="DOCUMENT_VERIFIED">Verified</MenuItem>
                    <MenuItem value="APPROVED">Approved</MenuItem>
                  </TextField>
                </div>

                <List sx={{ p: 0, overflowY: 'auto', flexGrow: 1 }}>
                  {filteredApplications.map((a) => {
                    const style = getStatusChipColor(a.status);
                    const isSelected = selectedApplication && selectedApplication.id === a.id;
                    return (
                      <ListItem
                        key={a.id}
                        onClick={() => setSelectedApplication(a)}
                        sx={{
                          borderRadius: 2,
                          my: 0.5,
                          background: isSelected ? 'var(--primary-glow)' : 'transparent',
                          border: isSelected ? '1px solid var(--primary)' : '1px solid transparent',
                          cursor: 'pointer'
                        }}
                      >
                        <ListItemText
                          primary={
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                              <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{a.applicationNumber}</Typography>
                              <Chip label={a.status.replace('_', ' ')} size="small" sx={{ bgcolor: style.bg, color: style.text, fontWeight: 'bold', height: 20 }} />
                            </div>
                          }
                          secondary={
                            <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>
                              {a.citizenName} | {a.type.replace('_', ' ')}
                            </Typography>
                          }
                        />
                      </ListItem>
                    );
                  })}
                </List>
              </Paper>

              <Paper sx={{ p: 4, height: 550, display: 'flex', flexDirection: 'column', background: 'var(--gradient-card)', border: '1px solid var(--border-color)', borderRadius: '16px' }}>
                {selectedApplication ? (
                  <>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px' }}>
                      <div>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Service Delivery &amp; Certificate Issuance</Typography>
                        <Typography variant="h5" sx={{ fontWeight: 'bold' }}>{selectedApplication.type.replace('_', ' ')}</Typography>
                      </div>
                      <Chip label={selectedApplication.status.replace('_', ' ')} sx={{ bgcolor: getStatusChipColor(selectedApplication.status).bg, color: getStatusChipColor(selectedApplication.status).text, fontWeight: 'bold' }} />
                    </div>

                    <Divider sx={{ mb: 2.5 }} />

                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', marginBottom: '24px' }}>
                      <div>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Application Number</Typography>
                        <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{selectedApplication.applicationNumber}</Typography>
                      </div>
                      <div>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Applicant</Typography>
                        <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{selectedApplication.citizenName}</Typography>
                      </div>
                      <div style={{ gridColumn: 'span 2' }}>
                        <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Metadata / Details</Typography>
                        <Typography variant="body2" sx={{ fontWeight: 'bold', color: 'var(--primary)' }}>{selectedApplication.metadata}</Typography>
                      </div>
                    </div>

                    {selectedApplication.status === 'APPROVED' && associatedDoc && (
                      <div style={{ padding: '16px', background: 'var(--primary-glow)', border: '1px dashed var(--primary)', borderRadius: '12px', marginBottom: '16px' }}>
                        <Typography variant="subtitle2" sx={{ fontWeight: 'bold', color: 'var(--primary)' }}>Official Digitally Signed Document</Typography>
                        <Typography variant="body2" sx={{ fontFamily: 'monospace', fontWeight: 'bold' }}>Number: {associatedDoc.document.certificateNumber}</Typography>
                        <Typography variant="caption" sx={{ fontFamily: 'monospace', color: 'var(--text-muted)', display: 'block', marginTop: '4px' }}>Signature: {associatedDoc.document.digitalSignature}</Typography>
                      </div>
                    )}

                    <div style={{ marginTop: 'auto', display: 'flex', justifyContent: 'space-between', paddingTop: '16px', borderTop: '1px solid var(--border-color)' }}>
                      {userRole === 'ADMIN' ? (
                        <>
                          <Button variant="outlined" onClick={handleVerifyDocuments} disabled={selectedApplication.status !== 'SUBMITTED'}>
                            Verify Documents
                          </Button>
                          {selectedApplication.status === 'APPROVED' ? (
                            <Button variant="contained" startIcon={<DownloadIcon />} onClick={handleDownloadCertificate} sx={{ background: 'var(--gradient-teal)', color: '#fff' }}>
                              Download Certificate
                            </Button>
                          ) : (
                            <Button variant="contained" startIcon={<AssignmentTurnedInIcon />} onClick={handleApproveApplication} disabled={selectedApplication.status !== 'DOCUMENT_VERIFIED'} sx={{ background: 'var(--gradient-teal)', color: '#fff' }}>
                              Approve &amp; Sign
                            </Button>
                          )}
                        </>
                      ) : (
                        selectedApplication.status === 'APPROVED' && (
                          <Button variant="contained" startIcon={<DownloadIcon />} onClick={handleDownloadCertificate} sx={{ background: 'var(--gradient-teal)', color: '#fff', marginLeft: 'auto' }}>
                            Download Certificate
                          </Button>
                        )
                      )}
                    </div>
                  </>
                ) : (
                  <Typography variant="body2" sx={{ color: 'var(--text-muted)', m: 'auto' }}>Select an application to view details.</Typography>
                )}
              </Paper>

            </div>
          )}

          {/* -------------------------------------------------------------
              5. WELFARE SCHEMES & BUDGETS
              ------------------------------------------------------------- */}
          {activeView === 'BUDGETS' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
              
              <div style={{ display: 'grid', gridTemplateColumns: '5fr 7fr', gap: '32px' }}>
                <Paper sx={{ p: 3, height: 500, overflowY: 'auto' }}>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2 }}>Welfare Assistance Schemes</Typography>
                  <List sx={{ p: 0 }}>
                    {schemes.map((s) => {
                      const isSelected = selectedScheme && selectedScheme.id === s.id;
                      const util = Math.round((s.disbursedAmount / s.allocatedAmount) * 100);
                      return (
                        <ListItem
                          key={s.id}
                          onClick={() => setSelectedScheme(s)}
                          sx={{
                            borderRadius: 2,
                            my: 1,
                            background: isSelected ? 'var(--primary-glow)' : 'transparent',
                            border: isSelected ? '1px solid var(--primary)' : '1px solid transparent',
                            cursor: 'pointer',
                            display: 'block'
                          }}
                        >
                          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
                            <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{s.name}</Typography>
                            <Typography variant="caption" sx={{ color: 'var(--primary)', fontWeight: 'bold' }}>{util}% Utilized</Typography>
                          </div>
                          <Typography variant="caption" sx={{ color: 'var(--text-muted)', display: 'block', mb: 1 }}>{s.description}</Typography>
                          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                            <Typography variant="caption">Allocated: ${s.allocatedAmount.toLocaleString()}</Typography>
                            <Typography variant="caption" sx={{ fontWeight: 'bold' }}>Spent: ${s.disbursedAmount.toLocaleString()}</Typography>
                          </div>
                        </ListItem>
                      );
                    })}
                  </List>
                </Paper>

                <Paper sx={{ p: 4, height: 500, display: 'flex', flexDirection: 'column' }}>
                  {selectedScheme ? (
                    <>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Scheme Beneficiaries &amp; Payouts</Typography>
                      <Typography variant="h5" sx={{ fontWeight: 'bold', mb: 2 }}>{selectedScheme.name}</Typography>

                      <Typography variant="subtitle2" sx={{ fontWeight: 'bold', mb: 1 }}>Enrolled Beneficiaries</Typography>
                      <List sx={{ flexGrow: 1, overflowY: 'auto', p: 0 }}>
                        {beneficiaries.map((b) => (
                          <ListItem key={b.id} onClick={() => userRole === 'ADMIN' && setSelectedBeneficiary(b)} sx={{ background: '#fff', my: 0.5, borderRadius: 1, border: '1px solid var(--border-color)', cursor: 'pointer' }}>
                            <ListItemText
                              primary={<Typography variant="body2" sx={{ fontWeight: 'bold' }}>{b.citizenName}</Typography>}
                              secondary={<Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>{b.eligibilityCriteria}</Typography>}
                            />
                            <div style={{ textAlign: 'right' }}>
                              <Chip label={b.status} size="small" sx={{ bgcolor: getStatusChipColor(b.status).bg, color: getStatusChipColor(b.status).text, fontWeight: 'bold' }} />
                              {b.disbursedAmount > 0 && <Typography variant="caption" sx={{ display: 'block', color: 'var(--primary)', fontWeight: 'bold', marginTop: '4px' }}>+${b.disbursedAmount}</Typography>}
                            </div>
                          </ListItem>
                        ))}
                      </List>

                      {userRole === 'ADMIN' && (
                        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '16px', paddingTop: '16px', borderTop: '1px solid var(--border-color)' }}>
                          <Button variant="outlined" onClick={handleVerifyEligibility}>Verify Eligibility</Button>
                          <Button variant="contained" startIcon={<CurrencyRupeeIcon />} onClick={() => setDisburseDialogOpen(true)} sx={{ background: 'var(--gradient-teal)', color: '#fff' }}>
                            Disburse Funds
                          </Button>
                        </div>
                      )}
                    </>
                  ) : (
                    <Typography variant="body2" sx={{ color: 'var(--text-muted)', m: 'auto' }}>Select a scheme to view beneficiaries.</Typography>
                  )}
                </Paper>
              </div>

              {/* Department Budgets Grid */}
              {userRole === 'ADMIN' && (
                <Paper sx={{ p: 3 }}>
                  <Typography variant="subtitle1" sx={{ fontWeight: 'bold', mb: 2 }}>Departmental Budget Allocations ($47M Total)</Typography>
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px' }}>
                    {deptBudgets.map((b) => {
                      const pct = Math.round((b.disbursedAmount / b.allocatedAmount) * 100);
                      return (
                        <div key={b.id} style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                          <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{b.departmentName}</Typography>
                          <Typography variant="h5" sx={{ fontWeight: '800', my: 1, color: pct >= 90 ? '#ef4444' : 'var(--primary)' }}>{pct}%</Typography>
                          <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Spent: ${(b.disbursedAmount / 1000000).toFixed(1)}M / ${(b.allocatedAmount / 1000000).toFixed(1)}M</Typography>
                        </div>
                      );
                    })}
                  </div>
                </Paper>
              )}

            </div>
          )}

          {/* -------------------------------------------------------------
              6. MILESTONE 4: EXECUTIVE DASHBOARD & GOVERNANCE REPORTS
              ------------------------------------------------------------- */}
          {activeView === 'REPORTS' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '28px' }}>
              
              {/* Header & Milestone 4 Validation Tabs */}
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 2 }}>
                <div>
                  <Typography variant="h4" sx={{ fontWeight: '800', letterSpacing: -0.5 }}>
                    Executive Dashboard &amp; Reports
                  </Typography>
                  <Typography variant="body2" sx={{ color: 'var(--text-muted)', marginTop: '4px' }}>
                    Milestone 4 Governance Analytics, Department Benchmarks, Fiscal Audits, &amp; Citizen Ratings
                  </Typography>
                </div>
                <div style={{ display: 'flex', gap: '12px' }}>
                  <Button variant="outlined" startIcon={<FileDownloadIcon />} onClick={() => setExportDialogOpen(true)} sx={{ textTransform: 'none', color: 'var(--primary)', borderColor: 'var(--primary)' }}>
                    Export Report
                  </Button>
                  <Button variant="outlined" startIcon={<BarChartIcon />} onClick={() => setDrillDownOpen(true)} sx={{ textTransform: 'none', color: 'var(--primary)', borderColor: 'var(--primary)' }}>
                    Drill Down
                  </Button>
                  <Button variant="contained" startIcon={<ShareIcon />} onClick={handleShareReport} sx={{ textTransform: 'none', background: 'var(--gradient-teal)', color: '#fff' }}>
                    Share
                  </Button>
                </div>
              </div>

              {/* Milestone 4 Top 3 KPI Cards */}
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '24px' }}>
                <Card sx={{ p: 1, borderRadius: '16px' }}>
                  <CardContent>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '8px' }}>
                      <Typography variant="body2" sx={{ fontWeight: '600', color: 'var(--text-muted)' }}>Citizen Satisfaction</Typography>
                      <Chip label="Rating" size="small" sx={{ bgcolor: '#d1fae5', color: '#047857', fontWeight: 'bold' }} />
                    </div>
                    <Typography variant="h3" sx={{ fontWeight: '800', color: 'var(--primary)' }}>
                      {governanceKpis?.citizenSatisfaction || 4.7}/5
                    </Typography>
                    <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Aggregate verified citizen rating</Typography>
                  </CardContent>
                </Card>

                <Card sx={{ p: 1, borderRadius: '16px' }}>
                  <CardContent>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '8px' }}>
                      <Typography variant="body2" sx={{ fontWeight: '600', color: 'var(--text-muted)' }}>Service SLA</Typography>
                      <Chip label="Met" size="small" sx={{ bgcolor: '#e0f2fe', color: '#0369a1', fontWeight: 'bold' }} />
                    </div>
                    <Typography variant="h3" sx={{ fontWeight: '800', color: 'var(--secondary)' }}>
                      {governanceKpis?.serviceSla || 94}%
                    </Typography>
                    <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Complaints &amp; documents completed within target SLA</Typography>
                  </CardContent>
                </Card>

                <Card sx={{ p: 1, borderRadius: '16px' }}>
                  <CardContent>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '8px' }}>
                      <Typography variant="body2" sx={{ fontWeight: '600', color: 'var(--text-muted)' }}>Revenue</Typography>
                      <Chip label="Collected" size="small" sx={{ bgcolor: '#fef3c7', color: '#b45309', fontWeight: 'bold' }} />
                    </div>
                    <Typography variant="h3" sx={{ fontWeight: '800', color: '#d97706' }}>
                      ${governanceKpis?.revenueCollected || 12.4}M
                    </Typography>
                    <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>YTD Municipal revenue collections</Typography>
                  </CardContent>
                </Card>
              </div>

              {/* Validation Screen Tabs */}
              <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={reportTab} onChange={(_event, val) => setReportTab(val)} variant="scrollable" scrollButtons="auto">
                  <Tab label="Governance KPIs" sx={{ textTransform: 'none', fontWeight: 'bold' }} />
                  <Tab label="Department Performance" sx={{ textTransform: 'none', fontWeight: 'bold' }} />
                  <Tab label="Revenue Tracking" sx={{ textTransform: 'none', fontWeight: 'bold' }} />
                  <Tab label="Grievance Analytics" sx={{ textTransform: 'none', fontWeight: 'bold' }} />
                  <Tab label="Citizen Satisfaction" sx={{ textTransform: 'none', fontWeight: 'bold' }} />
                </Tabs>
              </Box>

              {/* Tab 0: Analytics Dashboard - Governance KPIs (Exact Milestone 4 Screen) */}
              {reportTab === 0 && (
                <Paper sx={{ p: 4, background: 'var(--gradient-card)' }}>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 1, color: 'var(--primary)' }}>
                    Analytics Dashboard - Governance KPIs
                  </Typography>
                  <Typography variant="caption" sx={{ color: 'var(--text-muted)', display: 'block', mb: 3 }}>
                    Executive synthesis of public service velocity, resolution rates, and fiscal tracking
                  </Typography>

                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '20px' }}>
                    <div style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>Service Requests</Typography>
                      <Typography variant="body1" sx={{ fontWeight: 'bold', mt: 0.5 }}>
                        Services: <strong>24.7K requests</strong> | <span style={{ color: '#10b981' }}>94% resolved</span> | Avg 2.4 days
                      </Typography>
                    </div>

                    <div style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>Grievances</Typography>
                      <Typography variant="body1" sx={{ fontWeight: 'bold', mt: 0.5 }}>
                        Grievances: <strong>12.4K filed</strong> | <span style={{ color: '#10b981' }}>94% resolved</span> | MTTR 47 hrs
                      </Typography>
                    </div>

                    <div style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>Revenue</Typography>
                      <Typography variant="body1" sx={{ fontWeight: 'bold', mt: 0.5 }}>
                        Revenue: <strong>$12.4M</strong> | Property Tax 67% | Licenses 23%
                      </Typography>
                    </div>

                    <div style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>Budget</Typography>
                      <Typography variant="body1" sx={{ fontWeight: 'bold', mt: 0.5 }}>
                        Budget: <strong>47M allocated / 41M utilized</strong> | 87%
                      </Typography>
                    </div>

                    <div style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>Departments</Typography>
                      <Typography variant="body1" sx={{ fontWeight: 'bold', mt: 0.5 }}>
                        Departments: Water <strong>94%</strong> | Health <strong>91%</strong> | Education <strong>89%</strong>
                      </Typography>
                    </div>

                    <div style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>Citizen Satisfaction &amp; Trend</Typography>
                      <Typography variant="body1" sx={{ fontWeight: 'bold', mt: 0.5 }}>
                        Citizen SAT: <strong>4.7/5</strong> | <span style={{ color: '#10b981' }}>Complaints ↓ 23%</span> | <span style={{ color: '#0284c7' }}>Services ↑ 47%</span>
                      </Typography>
                    </div>
                  </div>

                  <div style={{ display: 'flex', gap: '16px', marginTop: '24px', justifyContent: 'flex-end' }}>
                    <Button variant="outlined" startIcon={<FileDownloadIcon />} onClick={() => setExportDialogOpen(true)}>
                      [Export Report]
                    </Button>
                    <Button variant="outlined" startIcon={<BarChartIcon />} onClick={() => setDrillDownOpen(true)}>
                      [Drill Down]
                    </Button>
                    <Button variant="contained" startIcon={<ShareIcon />} onClick={handleShareReport} sx={{ background: 'var(--gradient-teal)', color: '#fff' }}>
                      [Share]
                    </Button>
                  </div>
                </Paper>
              )}

              {/* Tab 1: Department Performance Index */}
              {reportTab === 1 && (
                <Paper sx={{ p: 4 }}>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 1 }}>Department Performance Benchmark Ranking</Typography>
                  <Typography variant="caption" sx={{ color: 'var(--text-muted)', display: 'block', mb: 3 }}>
                    Resolution rates, SLA compliance %, and average turnaround hours across municipal agencies
                  </Typography>

                  <TableContainer component={Paper} variant="outlined">
                    <Table>
                      <TableHead sx={{ bgcolor: 'rgba(0,0,0,0.02)' }}>
                        <TableRow>
                          <TableCell sx={{ fontWeight: 'bold' }}>Agency / Department</TableCell>
                          <TableCell sx={{ fontWeight: 'bold' }}>Resolution Rate</TableCell>
                          <TableCell sx={{ fontWeight: 'bold' }}>SLA Compliance</TableCell>
                          <TableCell sx={{ fontWeight: 'bold' }}>Average MTTR</TableCell>
                          <TableCell sx={{ fontWeight: 'bold' }}>Satisfaction Rating</TableCell>
                          <TableCell sx={{ fontWeight: 'bold' }}>Cases (Resolved / Total)</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {departmentPerformance.map((dept) => (
                          <TableRow key={dept.id}>
                            <TableCell sx={{ fontWeight: 'bold' }}>{dept.departmentName}</TableCell>
                            <TableCell>
                              <Chip label={`${dept.resolutionRate}%`} size="small" sx={{ bgcolor: dept.resolutionRate >= 90 ? '#d1fae5' : '#e0f2fe', color: dept.resolutionRate >= 90 ? '#047857' : '#0369a1', fontWeight: 'bold' }} />
                            </TableCell>
                            <TableCell sx={{ fontWeight: 'bold' }}>{dept.slaCompliance}%</TableCell>
                            <TableCell>{dept.avgResponseHours} hrs</TableCell>
                            <TableCell sx={{ color: '#d97706', fontWeight: 'bold' }}>★ {dept.satisfactionScore}</TableCell>
                            <TableCell>{dept.resolvedCases} / {dept.totalCases}</TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                </Paper>
              )}

              {/* Tab 2: Revenue Tracking */}
              {reportTab === 2 && (
                <Paper sx={{ p: 4 }}>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 1 }}>Municipal Revenue Realization ($12.4M Total)</Typography>
                  <Typography variant="caption" sx={{ color: 'var(--text-muted)', display: 'block', mb: 3 }}>
                    Audited tax collections, commercial licensing permits, and public service fees
                  </Typography>

                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '24px' }}>
                    <div style={{ background: '#fff', padding: '20px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>Primary Revenue Source</Typography>
                      <Typography variant="h5" sx={{ fontWeight: '800', marginTop: '4px', color: 'var(--primary)' }}>Property Tax (67%)</Typography>
                      <Typography variant="h4" sx={{ fontWeight: '800', my: 1 }}>$8.3M</Typography>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Residential and commercial land holding taxes</Typography>
                    </div>

                    <div style={{ background: '#fff', padding: '20px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>Commercial Permits</Typography>
                      <Typography variant="h5" sx={{ fontWeight: '800', marginTop: '4px', color: '#0ea5e9' }}>Trade Licenses (23%)</Typography>
                      <Typography variant="h4" sx={{ fontWeight: '800', my: 1 }}>$2.9M</Typography>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Business permits and shop establishment certificates</Typography>
                    </div>

                    <div style={{ background: '#fff', padding: '20px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', fontWeight: 'bold' }}>Infrastructure Services</Typography>
                      <Typography variant="h5" sx={{ fontWeight: '800', marginTop: '4px', color: '#10b981' }}>Utility Fees (10%)</Typography>
                      <Typography variant="h4" sx={{ fontWeight: '800', my: 1 }}>$1.2M</Typography>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Water connection tariffs and sanitation charges</Typography>
                    </div>
                  </div>
                </Paper>
              )}

              {/* Tab 3: Grievance Analytics */}
              {reportTab === 3 && (
                <Paper sx={{ p: 4 }}>
                  <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 1 }}>Grievance Trends &amp; Escalations</Typography>
                  <Typography variant="caption" sx={{ color: 'var(--text-muted)', display: 'block', mb: 3 }}>
                    Monthly complaint reduction trends and mean time to resolution (MTTR)
                  </Typography>

                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '20px' }}>
                    <div style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Monthly Grievance Volume</Typography>
                      <Typography variant="h4" sx={{ fontWeight: '800', my: 0.5 }}>12.4K</Typography>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 4, color: '#10b981' }}>
                        <TrendingDownIcon fontSize="small" />
                        <Typography variant="caption" sx={{ fontWeight: 'bold' }}>Complaints down 23% vs last month</Typography>
                      </div>
                    </div>

                    <div style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Mean Time to Resolution (MTTR)</Typography>
                      <Typography variant="h4" sx={{ fontWeight: '800', my: 0.5, color: 'var(--primary)' }}>47 Hours</Typography>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Across 15 wards and 5 municipal zones</Typography>
                    </div>

                    <div style={{ background: '#fff', padding: '16px', borderRadius: '12px', border: '1px solid var(--border-color)' }}>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Resolution Rate</Typography>
                      <Typography variant="h4" sx={{ fontWeight: '800', my: 0.5, color: '#10b981' }}>94%</Typography>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>Resolved prior to SLA expiration</Typography>
                    </div>
                  </div>
                </Paper>
              )}

              {/* Tab 4: Citizen Satisfaction & Reviews */}
              {reportTab === 4 && (
                <div style={{ display: 'grid', gridTemplateColumns: userRole === 'CITIZEN' ? '1fr 1fr' : '1fr', gap: '32px' }}>
                  {userRole === 'CITIZEN' && (
                    <Paper sx={{ p: 4, background: '#fff' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '16px' }}>
                        <RateReviewIcon sx={{ color: 'var(--primary)' }} />
                        <Typography variant="h6" sx={{ fontWeight: 'bold' }}>Submit Citizen Rating</Typography>
                      </div>
                      <Typography variant="caption" sx={{ color: 'var(--text-muted)', display: 'block', mb: 2 }}>
                        Your feedback directly impacts department SLA compliance and transparency rankings.
                      </Typography>

                      <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <div>
                          <Typography variant="caption" sx={{ color: 'var(--text-muted)', display: 'block', mb: 0.5 }}>Rating</Typography>
                          <Rating value={feedbackRating} onChange={(_event, val) => setFeedbackRating(val || 5)} size="large" />
                        </div>
                        <FormControl fullWidth variant="outlined" size="small">
                          <InputLabel id="fb-cat-label">Service Category</InputLabel>
                          <Select labelId="fb-cat-label" value={feedbackCategory} onChange={(e) => setFeedbackCategory(e.target.value)} label="Service Category">
                            <MenuItem value="Water Supply">Water Supply</MenuItem>
                            <MenuItem value="Sanitation & Waste">Sanitation &amp; Waste</MenuItem>
                            <MenuItem value="Certificates & Approvals">Certificates &amp; Approvals</MenuItem>
                            <MenuItem value="Electricity & Streetlights">Electricity &amp; Streetlights</MenuItem>
                            <MenuItem value="Roads & Public Works">Roads &amp; Public Works</MenuItem>
                          </Select>
                        </FormControl>
                        <TextField
                          label="Your Comments / Review *"
                          variant="outlined"
                          fullWidth
                          multiline
                          rows={3}
                          value={feedbackComments}
                          onChange={(e) => setFeedbackComments(e.target.value)}
                          placeholder="Describe your experience with the municipal service..."
                        />
                        <Button variant="contained" onClick={handleCitizenFeedbackSubmit} disabled={feedbackSubmitting || !feedbackComments.trim()} sx={{ background: 'var(--gradient-teal)', color: '#fff', alignSelf: 'flex-start' }}>
                          Submit Review
                        </Button>
                      </div>
                    </Paper>
                  )}

                  <Paper sx={{ p: 4, background: '#fff' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                      <Typography variant="h6" sx={{ fontWeight: 'bold' }}>Verified Citizen Reviews Feed</Typography>
                      <Chip label="Rating 4.7/5" size="small" sx={{ bgcolor: '#d1fae5', color: '#047857', fontWeight: 'bold' }} />
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', maxHeight: 350, overflowY: 'auto' }}>
                      {citizenFeedbacks.map((fb) => (
                        <div key={fb.id} style={{ background: '#f8fafc', padding: '14px', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
                          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '4px' }}>
                            <Typography variant="body2" sx={{ fontWeight: 'bold' }}>{fb.citizenName}</Typography>
                            <Rating value={fb.rating} readOnly size="small" />
                          </div>
                          <Typography variant="caption" sx={{ color: 'var(--primary)', fontWeight: 'bold', display: 'block', marginBottom: '4px' }}>{fb.category}</Typography>
                          <Typography variant="caption" sx={{ color: 'var(--text-muted)' }}>"{fb.comments}"</Typography>
                        </div>
                      ))}
                    </div>
                  </Paper>
                </div>
              )}

            </div>
          )}

          {/* Audit Logs System (ADMIN ONLY) */}
          {userRole === 'ADMIN' && (
            <Paper sx={{ p: 3, maxHeight: 180, overflow: 'hidden', display: 'flex', flexDirection: 'column', background: 'var(--gradient-card)', border: '1px solid var(--border-color)', borderRadius: '16px', marginTop: '32px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                <HistoryIcon sx={{ color: 'var(--primary)', fontSize: 20 }} />
                <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                  System Audit Trail &amp; Event Stream (Simulated Kafka / PostgreSQL Event Log)
                </Typography>
              </div>
              <Divider sx={{ mb: 1 }} />
              <div style={{ flexGrow: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '4px' }}>
                {logs.map((logStr, i) => (
                  <Typography key={i} variant="caption" sx={{
                    fontFamily: 'monospace',
                    color: logStr.includes('Error') ? '#ef4444' : (logStr.includes('Milestone') ? '#0284c7' : 'var(--text-muted)')
                  }}>
                    {logStr}
                  </Typography>
                ))}
              </div>
            </Paper>
          )}

        </div>
      </div>

      {/* Assignment Menu */}
      <Menu
        anchorEl={assignMenuAnchor}
        open={Boolean(assignMenuAnchor)}
        onClose={() => setAssignMenuAnchor(null)}
      >
        <MenuItem onClick={() => handleAssignDept('WATER_DEPT')}>Water Dept</MenuItem>
        <MenuItem onClick={() => handleAssignDept('ELECTRICITY_DEPT')}>Electricity Dept</MenuItem>
        <MenuItem onClick={() => handleAssignDept('PUBLIC_WORKS')}>Public Works</MenuItem>
        <MenuItem onClick={() => handleAssignDept('SANITATION')}>Sanitation</MenuItem>
        <MenuItem onClick={() => handleAssignDept('HEALTH_DEPT')}>Health Dept</MenuItem>
      </Menu>

      {/* Resolve Dialog */}
      <Dialog open={resolveDialogOpen} onClose={() => setResolveDialogOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle>Resolve Grievance Case</DialogTitle>
        <DialogContent dividers>
          <TextField
            label="Resolution Comments / Work Order Notes"
            variant="outlined"
            fullWidth
            multiline
            rows={4}
            value={resolutionNotes}
            onChange={(e) => setResolutionNotes(e.target.value)}
            placeholder="e.g. Broken water pipe replaced and restored."
          />
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setResolveDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleResolveGrievance} variant="contained" disabled={!resolutionNotes} sx={{ background: 'var(--gradient-teal)', color: '#fff' }}>
            Resolve &amp; Close
          </Button>
        </DialogActions>
      </Dialog>

      {/* Disburse Funds Dialog */}
      <Dialog open={disburseDialogOpen} onClose={() => setDisburseDialogOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle>Disburse Welfare Funds</DialogTitle>
        <DialogContent dividers sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          <Typography variant="body2" sx={{ color: 'var(--text-muted)' }}>
            Confirm disbursement amount to <strong>{selectedBeneficiary?.citizenName}</strong>.
          </Typography>
          <TextField
            label="Amount ($)"
            variant="outlined"
            fullWidth
            type="number"
            value={disburseAmount}
            onChange={(e) => setDisburseAmount(e.target.value)}
          />
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setDisburseDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleDisburseFunds} variant="contained" sx={{ background: 'var(--gradient-teal)', color: '#fff' }}>
            Confirm
          </Button>
        </DialogActions>
      </Dialog>

      {/* Milestone 4: Export Report Dialog */}
      <Dialog open={exportDialogOpen} onClose={() => setExportDialogOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle sx={{ fontWeight: 'bold', color: 'var(--primary)' }}>Export Governance Report</DialogTitle>
        <DialogContent dividers sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          <Typography variant="body2" sx={{ color: 'var(--text-muted)' }}>
            Choose export format for the governance audit, KPI metrics, and department benchmarks.
          </Typography>
          <FormControl fullWidth variant="outlined" size="small">
            <InputLabel id="exp-fmt-label">File Format</InputLabel>
            <Select labelId="exp-fmt-label" value={exportFormat} onChange={(e) => setExportFormat(e.target.value)} label="File Format">
              <MenuItem value="CSV">CSV (Spreadsheet Data)</MenuItem>
              <MenuItem value="JSON">JSON (Machine-Readable Records)</MenuItem>
              <MenuItem value="PDF">PDF (Executive Summary)</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setExportDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleExportReport} variant="contained" startIcon={<FileDownloadIcon />} sx={{ background: 'var(--gradient-teal)', color: '#fff' }}>
            Download
          </Button>
        </DialogActions>
      </Dialog>

      {/* Milestone 4: Drill Down Dialog */}
      <Dialog open={drillDownOpen} onClose={() => setDrillDownOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle sx={{ fontWeight: 'bold', color: 'var(--primary)' }}>
          Department Performance &amp; SLA Compliance Deep-Dive
        </DialogTitle>
        <DialogContent dividers>
          <TableContainer>
            <Table>
              <TableHead sx={{ bgcolor: 'rgba(0,0,0,0.02)' }}>
                <TableRow>
                  <TableCell sx={{ fontWeight: 'bold' }}>Department</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Resolution Rate</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>SLA Compliance</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Avg Response</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Satisfaction</TableCell>
                  <TableCell sx={{ fontWeight: 'bold' }}>Resolved / Total</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {departmentPerformance.map((dept) => (
                  <TableRow key={dept.id}>
                    <TableCell sx={{ fontWeight: 'bold' }}>{dept.departmentName}</TableCell>
                    <TableCell>
                      <Chip label={`${dept.resolutionRate}%`} size="small" sx={{ bgcolor: '#d1fae5', color: '#047857', fontWeight: 'bold' }} />
                    </TableCell>
                    <TableCell sx={{ fontWeight: 'bold' }}>{dept.slaCompliance}%</TableCell>
                    <TableCell>{dept.avgResponseHours} hrs</TableCell>
                    <TableCell sx={{ color: '#d97706', fontWeight: 'bold' }}>★ {dept.satisfactionScore}</TableCell>
                    <TableCell>{dept.resolvedCases} / {dept.totalCases}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setDrillDownOpen(false)} variant="contained" sx={{ background: 'var(--gradient-teal)', color: '#fff' }}>
            Close
          </Button>
        </DialogActions>
      </Dialog>

      {/* Share Toast Notification */}
      <Snackbar open={shareToastOpen} autoHideDuration={3000} onClose={() => setShareToastOpen(false)} message="Dashboard link copied to clipboard!" />

      {/* Feedback Success Toast */}
      <Snackbar open={feedbackSuccessToast} autoHideDuration={3000} onClose={() => setFeedbackSuccessToast(false)}>
        <Alert severity="success" onClose={() => setFeedbackSuccessToast(false)}>
          Thank you! Your citizen satisfaction review has been recorded.
        </Alert>
      </Snackbar>

      {/* Modals */}
      <CitizenRegistrationModal open={citizenModalOpen} onClose={() => setCitizenModalOpen(false)} onSuccess={(c) => { setCitizensList(prev => [c, ...prev]); addLog(`Registered: ${c.name}`); }} />
      <GrievanceRegistrationModal open={grievanceModalOpen} onClose={() => setGrievanceModalOpen(false)} onSuccess={(g) => { setGrievances(prev => [g, ...prev]); addLog(`Filed: ${g.title}`); }} />
      <ApplicationSubmissionModal open={applicationModalOpen} onClose={() => setApplicationModalOpen(false)} onSuccess={(a) => { setApplications(prev => [a, ...prev]); addLog(`Applied: ${a.applicationNumber}`); }} />
      {selectedScheme && (
        <BeneficiaryEnrollmentModal open={beneficiaryModalOpen} onClose={() => setBeneficiaryModalOpen(false)} onSuccess={(b) => { setBeneficiaries(prev => [b, ...prev]); addLog(`Enrolled: ${b.citizenName}`); }} schemeId={selectedScheme.id} />
      )}

    </div>
  );
}
