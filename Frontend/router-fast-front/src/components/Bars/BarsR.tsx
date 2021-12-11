import * as React from "react";
import Box from "@mui/material/Box";
import LinearProgress from "@mui/material/LinearProgress";
import { Card, CardContent, Typography } from "@mui/material";

const BarsR = () => {
  const [progress, setProgress] = React.useState(0);
  let tiempoSimulacion = 900; //colapso en 5 segundos
  let intervalito = tiempoSimulacion * 10;

  React.useEffect(() => {
    const timer = setInterval(() => {
      setProgress((oldProgress) => {
        return Math.min(oldProgress + 1, 100);
      });
    }, intervalito);

    return () => {
      clearInterval(timer);
    };
  }, []);

  return (
    <Card sx={{ mt: 2 }}>
        <CardContent> 
            <Typography variant="h4">
                Progreso de la simulación:
            </Typography>
            <Box sx={{ width: "100%" }}>
        <LinearProgress
            color="primary"
            variant="determinate"
            value={progress}
        />
        </Box>
       </CardContent>
    </Card>
    
  );
}
export default BarsR;