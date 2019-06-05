import React, { Component } from "react";
import DraggableList from "./draggable-list/draggable-list";
import Fab from "@material-ui/core/Fab";
import AddIcon from "@material-ui/icons/Add";
import { withStyles } from "@material-ui/core";

const styles = (theme: any) => ({
  fab: {
    position: "fixed",
    bottom: theme.spacing.unit * 2,
    right: theme.spacing.unit * 2
  }
});

interface AppProps {
  classes: any;
}
interface AppState {
  items: string[];
  order: number[];
}

class TodoItems extends Component<AppProps, AppState> {
  state = {
    items: ["a", "b", "c"],
    order: [0, 1, 2]
  };

  addNewTodoItem = () => {
    this.setState(state => ({
      items: [...state.items, `item-${state.items.length}`],
      order: [...state.order, state.order.length]
    }));
  };

  updateOrder = (newOrder: number[]) => {
    this.setState({ order: newOrder });
  };

  render() {
    return (
      <div>
        <DraggableList
          initialItems={this.state.items}
          updateOrderCallback={this.updateOrder}
          initialOrder={this.state.order}
        />
        <Fab
          color="primary"
          aria-label="Add"
          className={this.props.classes.fab}
          onClick={this.addNewTodoItem}
        >
          <AddIcon />
        </Fab>
      </div>
    );
  }
}

export default withStyles(styles as any)(TodoItems);
