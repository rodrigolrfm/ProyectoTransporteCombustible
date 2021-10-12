import { Box, Container, Link, Typography } from '@material-ui/core';
import { experimentalStyled } from '@material-ui/core/styles';

const FooterWrapper = experimentalStyled(Box)(
  ({ theme }) => `
        border-radius: 0;
        margin: ${theme.spacing(3)} 0;
`
);

function Footer() {
  return (
    <FooterWrapper>
      <Container maxWidth="lg">
        
      </Container>
    </FooterWrapper>
  );
}

export default Footer;
