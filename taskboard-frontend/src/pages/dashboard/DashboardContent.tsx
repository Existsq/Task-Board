import { Box } from "@mui/joy";
import TaskTable, { RowType } from "../../components/dashboard/tasks/TaskTable";
import Sidebar from "../../components/dashboard/sidebar/Sidebar";
import DashboardHeader from "./DashboardHeader";
import TaskFilters, { Filters } from "../../components/dashboard/tasks/TaskFilters";
import { useTasks } from "../../hooks/useTasks";
import { useState, useEffect } from "react";
import EditTaskModal from "../../components/dashboard/tasks/EditTaskModal";

export type Task = {
  id: string;
  title: string;
  description: string;
  dueDate: string;
  status: string;
};

export default function DashboardContent() {
  const [filters, setFilters] = useState<Filters>({});

  const { rows, loading, deleteTask, refresh, updateTask } = useTasks();

  useEffect(() => {
    refresh(filters);
  }, [filters, refresh]);

  const [editingTask, setEditingTask] = useState<Task | null>(null);

  const tableRows: RowType[] = rows.map((task) => ({
    id: task.id,
    title: task.title,
    description: task.description,
    status:
      task.status.charAt(0).toUpperCase() + task.status.slice(1).toLowerCase(),
    dueDate: task.dueDate,
    date: task.dueDate,
    until: task.dueDate,
  }));

  const handleDelete = async (id: string) => {
    await deleteTask(id);
    refresh();
  };

  const handleEdit = (id: string) => {
    const task = tableRows.find((r) => r.id === id);
    if (task) setEditingTask(task);
  };

  const handleUpdate = async (updatedTask: Task) => {
    await updateTask({
      ...updatedTask,
      id: String(updatedTask.id),
    });
    setEditingTask(null);
    refresh();
  };

  const handleCloseEdit = () => {
    setEditingTask(null);
  };

  return (
    <Box sx={{ display: "flex", minHeight: "100dvh" }}>
      <Sidebar onTaskCreated={refresh} />
      <Box
        component="main"
        className="MainContent"
        sx={{
          px: { xs: 2, md: 6 },
          pt: {
            xs: "calc(12px + var(--Header-height))",
            sm: "calc(12px + var(--Header-height))",
            md: 3,
          },
          pb: { xs: 2, sm: 2, md: 3 },
          flex: 1,
          display: "flex",
          flexDirection: "column",
          minWidth: 0,
          height: "100dvh",
          gap: 1,
          overflow: "auto",
        }}
      >
        <DashboardHeader />
        <TaskFilters filters={filters} onFilterChange={setFilters} />
        <TaskTable
          rows={tableRows}
          loading={loading}
          onDelete={handleDelete}
          onEdit={handleEdit}
        />
        {editingTask && (
          <EditTaskModal
            task={{
              id: editingTask.id,
              title: editingTask.title,
              description: editingTask.description,
              dueDate: editingTask.dueDate,
              status: editingTask.status,
            }}
            onClose={handleCloseEdit}
            onSave={handleUpdate}
          />
        )}
      </Box>
    </Box>
  );
}
