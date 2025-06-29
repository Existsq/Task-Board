import { extendTheme } from "@mui/joy/styles";

const theme = extendTheme({
  colorSchemes: {
    light: { palette: { mode: "light" } },
    dark: { palette: { mode: "dark" } },
  },
});

export default theme;