import { createTheme } from "@material-ui/core/styles";
import purple from "@material-ui/core/colors/purple";
import green from "@material-ui/core/colors/green";

const tema = createTheme({
  palette: {
    primary: purple,
    secondary: green,
  },
});

export default tema;