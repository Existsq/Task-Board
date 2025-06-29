import { CssVarsProvider } from "@mui/joy/styles";
import CssBaseline from "@mui/joy/CssBaseline";
import Box from "@mui/joy/Box";
import Header from "../../components/dashboard/Header";
import DashboardContent from "./DashboardContent";

export default function DashboardPage() {
  return (
    <CssVarsProvider disableTransitionOnChange>
      <CssBaseline />
      <Box sx={{ display: "flex", minHeight: "100dvh" }}>
        <Header />
        <DashboardContent />
      </Box>
    </CssVarsProvider>
  );
}