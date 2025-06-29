import { Sheet, Box, Button } from "@mui/joy";
import AddRoundedIcon from "@mui/icons-material/AddRounded";
import GlobalStyles from "@mui/joy/GlobalStyles";
import SidebarHeader from "./SidebarHeader";
import SidebarList from "./SidebarList";
import SidebarUserPanel from "./SidebarUserPanel";
import CreateTaskModal from "./CreateTaskModal";
import { closeSidebar } from "../../../lib/utils";
import React from "react";

interface SidebarProps {
  onTaskCreated: () => void;
}

export default function Sidebar({ onTaskCreated }: SidebarProps) {
  const [open, setOpen] = React.useState(false);

  return (
    <Sheet
      className="Sidebar"
      sx={{
        position: { xs: "fixed", md: "sticky" },
        transform: {
          xs: "translateX(calc(100% * (var(--SideNavigation-slideIn, 0) - 1)))",
          md: "none",
        },
        transition: "transform 0.4s, width 0.4s",
        zIndex: 10000,
        height: "100dvh",
        width: "var(--Sidebar-width)",
        top: 0,
        p: 2,
        flexShrink: 0,
        display: "flex",
        flexDirection: "column",
        gap: 2,
        borderRight: "1px solid",
        borderColor: "divider",
      }}
    >
      <GlobalStyles styles={(theme) => ({
        ":root": {
          "--Sidebar-width": "220px",
          [theme.breakpoints.up("lg")]: {
            "--Sidebar-width": "240px",
          },
        },
      })} />

      <Box className="Sidebar-overlay" sx={{
        position: "fixed", zIndex: 9998, top: 0, left: 0,
        width: "100vw", height: "100vh",
        opacity: "var(--SideNavigation-slideIn)",
        backgroundColor: "var(--joy-palette-background-backdrop)",
        transition: "opacity 0.4s",
        transform: {
          xs: "translateX(calc(100% * (var(--SideNavigation-slideIn, 0) - 1) + var(--SideNavigation-slideIn, 0) * var(--Sidebar-width, 0px)))",
          lg: "translateX(-100%)",
        },
      }} onClick={closeSidebar} />

      <SidebarHeader />
      <Button
        startDecorator={<AddRoundedIcon />}
        size="sm"
        fullWidth
        onClick={() => setOpen(true)}
      >
        Новая задача
      </Button>

      <SidebarList />
      <SidebarUserPanel />

      <CreateTaskModal open={open} onClose={() => setOpen(false)} onTaskCreated={onTaskCreated} />
    </Sheet>
  );
}