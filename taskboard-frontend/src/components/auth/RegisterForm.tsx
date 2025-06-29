import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Input,
  Stack,
  Typography,
} from "@mui/joy";
import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useNavigate, Link } from "react-router-dom";

export default function RegisterForm() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await register(email.toLowerCase(), password);
      navigate("/dashboard");
    } catch (err: any) {
      if (err.response && err.response.data) {
        setError(err.response.data.description || "Ошибка регистрации");
      } else {
        setError(err.message || "Произошла неизвестная ошибка");
      }
    }
  };

  return (
    <Box
      component="main"
      sx={{
        my: "auto",
        py: 2,
        pb: 5,
        width: 400,
        maxWidth: "100%",
        mx: "auto",
        borderRadius: "sm",
      }}
    >
      <form onSubmit={handleSubmit}>
        <Stack spacing={3}>
          <Typography level="h3">Регистрация</Typography>

          {error && (
            <Typography color="danger" level="body-sm">
              {error}
            </Typography>
          )}

          <FormControl required>
            <FormLabel>Email</FormLabel>
            <Input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="email@example.com"
            />
          </FormControl>

          <FormControl required>
            <FormLabel>Пароль</FormLabel>
            <Input
              type="password"
              placeholder="*******"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </FormControl>

          <Box display="flex" justifyContent="space-between">
            <Typography level="body-sm">
              <Link to="/login">Есть аккаунт?</Link>
            </Typography>
          </Box>

          <Button type="submit" fullWidth>
            Зарегистрироваться
          </Button>
        </Stack>
      </form>
    </Box>
  );
}
