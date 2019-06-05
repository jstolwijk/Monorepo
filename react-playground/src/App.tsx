import React from "react";
import CssBaseline from "@material-ui/core/CssBaseline";
import SignIn from "./pages/SignIn";
import { Router, Route } from "react-router-dom";
import { createBrowserHistory } from "history";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import IconButton from "@material-ui/core/IconButton";
import MenuIcon from "@material-ui/icons/Menu";
import { makeStyles } from "@material-ui/core/styles";

export const history = createBrowserHistory();

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1
  },
  menuButton: {
    marginRight: theme.spacing(2)
  },
  title: {
    flexGrow: 1
  }
}));

const App: React.FC = () => {
  const classes = useStyles();

  return (
    <Router history={history}>
      <CssBaseline />
      <Route path="/" exact component={() => <h1>root</h1>} />
      <Route path="/sign-in" component={SignIn} />
      <Route
        path="/dashboard"
        component={() => (
          <AppBar position="static">
            <Toolbar>
              <IconButton
                edge="start"
                className={classes.menuButton}
                color="inherit"
                aria-label="Menu"
              >
                <MenuIcon />
              </IconButton>
              <Typography variant="h6" className={classes.title}>
                News
              </Typography>
              <Button color="inherit">Login</Button>
            </Toolbar>
          </AppBar>
        )}
      />
    </Router>
  );
};

export default App;
