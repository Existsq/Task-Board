import { Box, Input, List, ListItem, ListItemButton, ListItemContent, Typography } from "@mui/joy";
import SearchRoundedIcon from "@mui/icons-material/SearchRounded";
import AssignmentRoundedIcon from "@mui/icons-material/AssignmentRounded";
import { listItemButtonClasses } from "@mui/joy/ListItemButton";

export default function SidebarList() {
  return (
    <Box
      sx={{
        minHeight: 0,
        overflow: "hidden auto",
        flexGrow: 1,
        display: "flex",
        flexDirection: "column",
        [`& .${listItemButtonClasses.root}`]: { gap: 1.5 },
      }}
    >
      <Input size="sm" startDecorator={<SearchRoundedIcon />} placeholder="Search" />
      <List
        size="sm"
        sx={{
          gap: 1,
          "--List-nestedInsetStart": "30px",
          "--ListItem-radius": (theme) => theme.vars.radius.sm,
        }}
      >
        <ListItem>
          <ListItemButton selected>
            <AssignmentRoundedIcon />
            <ListItemContent>
              <Typography level="title-sm">Tasks</Typography>
            </ListItemContent>
          </ListItemButton>
        </ListItem>
      </List>
    </Box>
  );
}