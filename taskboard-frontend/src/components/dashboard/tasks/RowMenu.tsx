import { Dropdown, MenuButton, Menu, MenuItem, Divider, IconButton } from "@mui/joy";
import MoreHorizRoundedIcon from "@mui/icons-material/MoreHorizRounded";

interface RowMenuProps {
  onDelete: () => void;
  onEdit: () => void;
}

export default function RowMenu({ onDelete, onEdit }: RowMenuProps) {
  return (
    <Dropdown>
      <MenuButton slots={{ root: IconButton }} slotProps={{ root: { variant: "plain", color: "neutral", size: "sm" } }}>
        <MoreHorizRoundedIcon />
      </MenuButton>
      <Menu size="sm" sx={{ minWidth: 140 }}>
        <MenuItem onClick={onEdit}>Edit</MenuItem>
        <Divider />
        <MenuItem color="danger" onClick={onDelete}>Delete</MenuItem>
      </Menu>
    </Dropdown>
  );
}