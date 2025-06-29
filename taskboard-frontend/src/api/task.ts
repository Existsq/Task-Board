const API_BASE = "/api/v1/tasks";

export type TaskResponse = {
  id: number;
  title: string;
  description: string;
  createdAt: string;
  updatedAt: string;
  status: string;
  dueDate: string;
};

function formatDueDate(dueDate: string): string {
  const date = new Date(dueDate);
  const pad = (n: number) => n.toString().padStart(2, "0");

  const month = pad(date.getMonth() + 1);
  const day = pad(date.getDate());
  const year = date.getFullYear();
  const hours = pad(date.getHours());
  const minutes = pad(date.getMinutes());

  return `${month}/${day}/${year} ${hours}:${minutes}`;
}

export async function getTasks(params?: Record<string, string>) {
  const token = localStorage.getItem("jwt");

  const query = params
    ? "?" + new URLSearchParams(params).toString()
    : "";

  const res = await fetch(`${API_BASE}${query}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) throw new Error("Ошибка при получении задач");
  return res.json();
}

export async function deleteTask(id: number): Promise<void> {
  const token = localStorage.getItem("jwt");
  const res = await fetch(`${API_BASE}/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    throw new Error("Не удалось удалить задачу");
  }
}

export async function createTask(task: {
  title: string;
  description: string;
  dueDate: string;
  status: string;
}) {
  const token = localStorage.getItem("jwt");

  const formattedTask = {
    ...task,
    dueDate: formatDueDate(task.dueDate),
  };

  const res = await fetch(API_BASE, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(formattedTask),
  });

  if (!res.ok) {
    throw new Error("Не удалось создать задачу");
  }

  return res.json();
}

export async function updateTask(task: {
  id: number;
  title: string;
  description: string;
  dueDate: string;
  status: string;
}) {
  const token = localStorage.getItem("jwt");

  const formattedTask = {
    ...task,
    dueDate: formatDueDate(task.dueDate),
    status: task.status.toUpperCase(),
  };

  const res = await fetch(`${API_BASE}/${task.id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(formattedTask),
  });

  if (!res.ok) {
    throw new Error("Не удалось обновить задачу");
  }

  return res.json();
}