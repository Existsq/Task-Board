import React, { useState } from "react";
import {
  Modal,
  ModalDialog,
  ModalClose,
  FormControl,
  FormLabel,
  Input,
  Textarea,
  Select,
  Option,
  Button,
  Typography,
} from "@mui/joy";

type Task = {
  id: string;
  title: string;
  description: string;
  dueDate: string;
  status: string;
};

export default function EditTaskModal({
  task,
  onClose,
  onSave,
}: {
  task: Task;
  onClose: () => void;
  onSave: (task: Task) => void;
}) {
  const [title, setTitle] = useState(task.title);
  const [description, setDescription] = useState(task.description);
  const [dueDate, setDueDate] = useState(task.dueDate ? task.dueDate.substring(0, 10) : "");
  const [status, setStatus] = useState(task.status);

  const handleSubmit = () => {
    if (!title.trim()) {
      alert("Введите название задачи");
      return;
    }

    onSave({
      ...task,
      title,
      description,
      dueDate,
      status,
    });
  };

  return (
    <Modal open onClose={onClose}>
      <ModalDialog>
        <ModalClose />
        <Typography level="h4" mb={2}>
          Редактировать задачу
        </Typography>

        <FormControl>
          <FormLabel>Название</FormLabel>
          <Input value={title} onChange={(e) => setTitle(e.target.value)} />
        </FormControl>

        <FormControl>
          <FormLabel>Описание</FormLabel>
          <Textarea
            value={description}
            minRows={3}
            onChange={(e) => setDescription(e.target.value)}
          />
        </FormControl>

        <FormControl>
          <FormLabel>Дата выполнения</FormLabel>
          <Input
            type="date"
            value={dueDate}
            onChange={(e) => setDueDate(e.target.value)}
          />
        </FormControl>

        <FormControl>
          <FormLabel>Статус</FormLabel>
          <Select
            value={status}
            onChange={(_, value) => value && setStatus(value)}
          >
            <Option value="NEW">New</Option>
            <Option value="IN_PROGRESS">In progress</Option>
            <Option value="COMPLETED">Completed</Option>
          </Select>
        </FormControl>

        <Button sx={{ mt: 2 }} onClick={handleSubmit}>
          Сохранить
        </Button>
      </ModalDialog>
    </Modal>
  );
}