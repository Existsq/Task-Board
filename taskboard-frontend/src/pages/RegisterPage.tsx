// pages/LoginPage.tsx
import { CssVarsProvider } from "@mui/joy/styles";
import GlobalStyles from "@mui/joy/GlobalStyles";
import Box from "@mui/joy/Box";
import Typography from "@mui/joy/Typography";
import theme from "../utils/theme";
import ColorSchemeToggle from "../components/theme/ColorSchemeToggle";
import BadgeRoundedIcon from "@mui/icons-material/BadgeRounded";
import IconButton from "@mui/joy/IconButton";
import { CssBaseline } from "@mui/joy";
import RegisterForm from "../components/auth/RegisterForm";

export default function RegisterPage() {
  return (
    <CssVarsProvider theme={theme} disableTransitionOnChange>
      <CssBaseline />
      <GlobalStyles styles={{ ":root": { "--Form-maxWidth": "800px" } }} />

      <Box
        sx={{
          width: { xs: "100%", md: "50vw" },
          display: "flex",
          justifyContent: "flex-end",
          backdropFilter: "blur(12px)",
          backgroundColor: "rgba(255 255 255 / 0.2)",
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            minHeight: "100dvh",
            width: "100%",
            px: 2,
          }}
        >
          <Box
            component="header"
            sx={{ py: 3, display: "flex", justifyContent: "space-between" }}
          >
            <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
              <IconButton variant="soft" color="primary" size="sm">
                <BadgeRoundedIcon />
              </IconButton>
              <Typography level="title-lg">TaskBoard</Typography>
            </Box>
            <ColorSchemeToggle />
          </Box>

          <RegisterForm />

          <Box component="footer" sx={{ py: 3 }}>
            <Typography level="body-xs" textAlign="center">
              © TaskBoard {new Date().getFullYear()}
            </Typography>
          </Box>
        </Box>
      </Box>

      <Box
        sx={{
          height: "100%",
          position: "fixed",
          left: { xs: 0, md: "50vw" },
          right: 0,
          top: 0,
          bottom: 0,
          backgroundImage:
            "url(https://images.unsplash.com/photo-1527181152855-fc03fc7949c8?auto=format&w=1000&dpr=2)",
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      />
    </CssVarsProvider>
  );
}
