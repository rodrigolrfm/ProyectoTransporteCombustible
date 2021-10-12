import { Box, Tooltip } from '@material-ui/core';
import { Link } from 'react-router-dom';
import { experimentalStyled } from '@material-ui/core/styles';

const LogoWrapper = experimentalStyled(Link)(
  ({ theme }) => `
        color: ${theme.palette.text.primary};
        display: flex;
        text-decoration: none;
        width: 53px;
        margin: 0 auto;
        font-weight: ${theme.typography.fontWeightBold};
`
);

const LogoSignWrapper = experimentalStyled(Box)(
  () => `
        width: 52px;
        height: 38px;
`
);

const LogoSign = experimentalStyled(Box)(
  ({ theme }) => `
        background: ${theme.general.reactFrameworkColor};
        width: 18px;
        height: 18px;
        border-radius: ${theme.general.borderRadiusSm};
        position: relative;
        transform: rotate(45deg);
        top: 3px;
        left: 17px;

        &:after, 
        &:before {
            content: "";
            display: block;
            width: 18px;
            height: 18px;
            position: absolute;
            top: -1px;
            right: -20px;
            transform: rotate(0deg);
            border-radius: ${theme.general.borderRadiusSm};
        }

        &:before {
            background: ${theme.palette.primary.main};
            right: auto;
            left: 0;
            top: 20px;
        }

        &:after {
            background: ${theme.palette.secondary.main};
        }
`
);

const LogoSignInner = experimentalStyled(Box)(
  ({ theme }) => `
        width: 16px;
        height: 16px;
        position: absolute;
        top: 12px;
        left: 12px;
        z-index: 5;
        border-radius: ${theme.general.borderRadiusSm};
        background: ${theme.header.background};
`
);

function Logo() {

  return (
    <Tooltip title="Soda Infiel" arrow>
      <LogoWrapper to="/overview">
        <LogoSignWrapper>
            <img src="https://www.google.com/url?sa=i&url=https%3A%2F%2Fsomoskudasai.com%2Fnoticias%2Fanime%2Fkanojo-okarishimasu-celebra-el-cumpleanos-de-ruka-con-una-ilustracion%2F&psig=AOvVaw0_wEJVLh_F8cp1rH3997lY&ust=1633062061770000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCNiV2bLspfMCFQAAAAAdAAAAABAD" alt="" />
        </LogoSignWrapper>
      </LogoWrapper>
    </Tooltip>
  );
}

export default Logo;
