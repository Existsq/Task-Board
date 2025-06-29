import {
  Box,
  Table,
  Chip,
  Typography,
  Sheet,
  CircularProgress,
} from "@mui/joy";
import { ColorPaletteProp } from "@mui/joy/styles";
import CheckRoundedIcon from "@mui/icons-material/CheckRounded";
import AutorenewRoundedIcon from "@mui/icons-material/AutorenewRounded";
import SwitchAccessShortcutIcon from "@mui/icons-material/SwitchAccessShortcut";
import RowMenu from "./RowMenu";

export type RowType = {
  dueDate: string;
  description: string;
  id: string;
  title: string;
  date: string;
  status: string;
  until: string;
};

interface TaskTableProps {
  rows: RowType[];
  loading: boolean;
  onDelete: (id: string) => void;
  onEdit: (id: string) => void;
}

export default function TaskTable({
  rows,
  loading,
  onDelete,
  onEdit,
}: TaskTableProps) {
  console.log("TaskTable rows:", rows); // Debug: Log rows prop

  return (
    <Box sx={{ display: { xs: "none", sm: "block" }, minHeight: 0 }}>
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", py: 4 }}>
          <CircularProgress />
          <Typography level="title-sm" sx={{ ml: 2 }}>Loading...</Typography>
        </Box>
      ) : rows.length === 0 ? (
        <Typography level="body-md" sx={{ textAlign: "center", py: 4 }}>
          No tasks found.
        </Typography>
      ) : (
        <Sheet
          variant="outlined"
          sx={{
            width: "100%",
            borderRadius: "sm",
            flexShrink: 1,
            overflow: "auto",
            minHeight: 0,
          }}
        >
          <Table
            stickyHeader
            hoverRow
            sx={{
              "--TableCell-headBackground": "var(--joy-palette-background-level1)",
              "--Table-headerUnderlineThickness": "1px",
              "--TableRow-hoverBackground": "var(--joy-palette-background-level1)",
              "--TableCell-paddingY": "4px",
              "--TableCell-paddingX": "8px",
            }}
          >
            <thead>
              <tr>
                <th style={{ width: "100px", paddingLeft: "16px" }}>Task</th>
                <th style={{ width: "150px" }}>Title</th>
                <th style={{ width: "250px" }}>Description</th>
                <th style={{ width: "120px" }}>Date</th>
                <th style={{ width: "130px" }}>Status</th>
                <th style={{ width: "120px" }}>Until</th>
                <th style={{ width: "60px" }}></th>
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => (
                <tr key={row.id}>
                  <td style={{ width: "100px", paddingLeft: "16px" }}>{row.id}</td>
                  <td
                    style={{
                      width: "150px",
                      whiteSpace: "nowrap",
                      overflow: "hidden",
                      textOverflow: "ellipsis",
                    }}
                  >
                    {row.title}
                  </td>
                  <td
                    style={{
                      width: "250px",
                      whiteSpace: "nowrap",
                      overflow: "hidden",
                      textOverflow: "ellipsis",
                    }}
                  >
                    {row.description}
                  </td>
                  <td style={{ width: "120px" }}>{row.date}</td>
                  <td style={{ width: "130px" }}>
                    <Chip
                      variant="soft"
                      size="sm"
                      startDecorator={
                        {
                          New: <SwitchAccessShortcutIcon />,
                          In_progress: <AutorenewRoundedIcon />,
                          Completed: <CheckRoundedIcon />,
                        }[row.status]
                      }
                      color={
                        {
                          New: "success",
                          In_progress: "neutral",
                          Completed: "primary",
                        }[row.status] as ColorPaletteProp
                      }
                    >
                      {row.status}
                    </Chip>
                  </td>
                  <td style={{ width: "120px" }}>
                    {row.until ? row.until.substring(0, 10) : ""}
                  </td>
                  <td style={{ width: "60px" }}>
                    <RowMenu
                      onDelete={() => onDelete(row.id)}
                      onEdit={() => onEdit(row.id)}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Sheet>
      )}
    </Box>
  );
}