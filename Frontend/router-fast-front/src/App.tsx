import { Button } from "@material-ui/core";
import React from "react";
import {ThemeProvider} from '@material-ui/core'
import tema from './temaConfig'
function App() {
  return (
    <ThemeProvider theme={tema}>
      <Button color="primary" variant="contained"> Holaaaa </Button>
    </ThemeProvider>
  );
}

export default App;
