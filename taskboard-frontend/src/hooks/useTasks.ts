import { useCallback, useState } from "react";
import {
  getTasks,
  deleteTask as apiDeleteTask,
  updateTask as apiUpdateTask,
  TaskResponse,
} from "../api/task";
import { Filters } from "../components/dashboard/tasks/TaskFilters";

type RowType = {
  dueDate: string;
  description: string;
  id: string;
  title: string;
  date: string;
  status: string;
  until: string;
};

export function useTasks() {
  const [rows, setRows] = useState<RowType[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  const refresh = useCallback(async (filters?: Filters) => {
    setLoading(true);
    setError(null);

    try {
      const params: Record<string, string> = {};

      if (filters?.status) params.status = filters.status;
      if (filters?.sort) params.sort = filters.sort;
      if (filters?.direction) params.order = filters.direction;

      const data = await getTasks(params);

      const mappedRows: RowType[] = data.map((task: TaskResponse) => ({
        id: String(task.id),
        title: task.title,
        description: task.description,
        status: task.status.charAt(0).toUpperCase() + task.status.slice(1).toLowerCase(),
        dueDate: task.dueDate,
        date: task.createdAt || task.dueDate,
        until: task.dueDate,
      }));

      setRows(mappedRows);
    } catch (err) {
      setError(err as Error);
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, []);

  const deleteTask = async (id: string) => {
    await apiDeleteTask(Number(id));
    await refresh();
  };

  const updateTask = async (task: {
    id: string;
    title: string;
    description: string;
    dueDate: string;
    status: string;
  }) => {
    await apiUpdateTask({
      ...task,
      id: Number(task.id),
    });
    await refresh();
  };

  return { rows, loading, error, refresh, deleteTask, updateTask };
}