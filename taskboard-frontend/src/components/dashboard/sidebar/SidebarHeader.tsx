import { Box, IconButton, Typography } from "@mui/joy";
import BrightnessAutoRoundedIcon from "@mui/icons-material/BrightnessAutoRounded";
import ColorSchemeToggle from "../../theme/ColorSchemeToggle";

export default function SidebarHeader() {
  return (
    <Box sx={{ display: "flex", gap: 1, alignItems: "center" }}>
      <IconButton variant="soft" color="primary" size="sm">
        <BrightnessAutoRoundedIcon />
      </IconButton>
      <Typography level="title-lg">TaskBoard</Typography>
      <ColorSchemeToggle sx={{ ml: "auto" }} />
    </Box>
  );
}