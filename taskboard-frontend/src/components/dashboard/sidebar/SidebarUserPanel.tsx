import { Box, Typography, Avatar, IconButton, Divider } from "@mui/joy";
import LogoutRoundedIcon from "@mui/icons-material/LogoutRounded";
import { useAuth } from "../../../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function SidebarUserPanel() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <>
      <Divider />
      <Box sx={{ display: "flex", gap: 1, alignItems: "center" }}>
        <Avatar variant="outlined" size="sm" />
        <Box sx={{ minWidth: 0, flex: 1 }}>
          <Typography level="title-sm">{user?.email.split("@")[0]}</Typography>
          <Typography level="body-xs">{user?.email}</Typography>
        </Box>
        <IconButton
          size="sm"
          variant="plain"
          color="neutral"
          onClick={() => {
            logout();
            navigate("/login");
          }}
        >
          <LogoutRoundedIcon />
        </IconButton>
      </Box>
    </>
  );
}