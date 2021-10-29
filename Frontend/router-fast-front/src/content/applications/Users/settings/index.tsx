import { useState, ChangeEvent } from 'react';
import { Helmet } from 'react-helmet-async';
import PageHeader from './PageHeader';
import PageTitleWrapper from 'src/components/PageTitleWrapper';
import { Container, Tabs, Tab, Grid, Card} from '@mui/material';
import Footer from 'src/components/Footer';
import { styled } from '@mui/material/styles';

import ActivityTab from './ActivityTab';
import EditProfileTab from './EditProfileTab';
import NotificationsTab from './NotificationsTab';
import SecurityTab from './SecurityTab';


function ManagementUserSettings() {

  return (
    <>
      <Helmet>
        <title>Lista de Usuarios</title>
      </Helmet>
      <Container>
        <Card>
          Lista de Usuarios
        </Card>
      </Container>
    </>
  );
}

export default ManagementUserSettings;
