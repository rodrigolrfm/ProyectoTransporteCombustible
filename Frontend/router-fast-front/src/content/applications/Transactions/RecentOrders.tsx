import { Card } from '@mui/material';
import { CryptoOrder } from 'src/models/crypto_order';
import RecentOrdersTable from './RecentOrdersTable';
import { subDays } from 'date-fns';
import MapR from 'src/components/MapR/MapR';
function RecentOrders() {
  return (
    <><Card>
      </Card>
     <MapR simulacion={1}></MapR>
      </>
  );
}

export default RecentOrders;
