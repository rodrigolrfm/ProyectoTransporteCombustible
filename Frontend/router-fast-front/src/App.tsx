import { Button } from "@mui/material";
import React from "react";
import ButtonUnstyled from "@mui/core/ButtonUnstyled";
function App() {
  return (
    <div className="App">
      <Button variant="text">Text</Button>
      <Button variant="contained" color="secondary">
        Contained
      </Button>
      <Button variant="outlined" color="secondary" size="large">
        Outlined
      </Button>
    </div>
  );
}

export default App;
