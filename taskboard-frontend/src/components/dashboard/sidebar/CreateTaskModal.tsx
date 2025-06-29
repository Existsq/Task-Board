import {
  Modal,
  ModalDialog,
  ModalClose,
  Typography,
  FormControl,
  FormLabel,
  Input,
  Textarea,
  Select,
  Option,
  Button,
} from "@mui/joy";
import React from "react";
import { createTask } from "../../../api/task";

interface CreateTaskModalProps {
  open: boolean;
  onClose: () => void;
  onTaskCreated: () => void;
}

export default function CreateTaskModal({ open, onClose, onTaskCreated }: CreateTaskModalProps) {
  const [title, setTitle] = React.useState("");
  const [description, setDescription] = React.useState("");
  const [dueDate, setDueDate] = React.useState("");
  const [status, setStatus] = React.useState<"NEW" | "IN_PROGRESS" | "COMPLETED">("NEW");

  const handleCreate = async () => {
    if (!title.trim()) {
      alert("Введите название задачи");
      return;
    }

    try {
      await createTask({ title, description, status, dueDate });
      onClose();
      setTitle("");
      setDescription("");
      setDueDate("");
      setStatus("NEW");
      onTaskCreated();
    } catch (err) {
      console.error(err);
      alert("Ошибка при создании задачи");
    }
  };

  return (
    <Modal open={open} onClose={onClose}>
      <ModalDialog>
        <ModalClose />
        <Typography level="h4">Создать новую задачу</Typography>

        <FormControl>
          <FormLabel>Название</FormLabel>
          <Input value={title} onChange={(e) => setTitle(e.target.value)} />
        </FormControl>

        <FormControl>
          <FormLabel>Описание</FormLabel>
          <Textarea minRows={3} value={description} onChange={(e) => setDescription(e.target.value)} />
        </FormControl>

        <FormControl>
          <FormLabel>Срок выполнения</FormLabel>
          <Input type="date" value={dueDate} onChange={(e) => setDueDate(e.target.value)} />
        </FormControl>

        <FormControl>
          <FormLabel>Статус</FormLabel>
          <Select value={status} onChange={(_, value) => value && setStatus(value)}>
            <Option value="NEW">New</Option>
            <Option value="IN_PROGRESS">In progress</Option>
            <Option value="COMPLETED">Completed</Option>
          </Select>
        </FormControl>

        <Button onClick={handleCreate}>Создать</Button>
      </ModalDialog>
    </Modal>
  );
}