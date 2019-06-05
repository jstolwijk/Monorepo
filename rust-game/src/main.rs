extern crate pancurses;
use pancurses::{endwin, initscr, noecho, Input, Window};
use rand::Rng;
use std::{thread, time};

#[macro_use]
extern crate strum_macros;
use strum_macros::{Display, EnumIter}; // etc.

use strum::IntoEnumIterator;

// tetris grid 10 * 20

static WIDTH: i32 = 10;
static HEIGHT: i32 = 20;

fn main() {
    let window = initscr();
    window.printw("Type things, press delete to quit\n");
    window.refresh();
    window.keypad(true);
    noecho();
    let tick_duration = time::Duration::from_millis(1000);
    let mut rng = rand::thread_rng();

    let block_options = Tetriminos::iter().collect::<Vec<Tetriminos>>();

    let world_size = (WIDTH * HEIGHT) as usize;
    let mut world: Vec<u32> = vec_of(0, world_size);

    loop {
        window.clear();

        world[get_index(0, 4)] = 1;
        world[get_index(0, 5)] = 1;
        world[get_index(1, 4)] = 1;
        world[get_index(1, 5)] = 1;

        draw_world(&window, &world);

        let block = rng.choose(&block_options);
        window.mvaddstr(0, 6, block.unwrap().to_string());

        // let amount = (rng.gen::<i32>() % 100).abs() + 1;
        // let block = rng.choose(&block_options);

        // for _ in times(amount as usize) {
        //     let x = (rng.gen::<i32>() % 50).abs() + 1;
        //     let y = (rng.gen::<i32>() % 50).abs() + 1;

        //     window.mvaddstr(x, y, block.unwrap().to_string());
        //     window.mv(0, 0);
        // }
        window.refresh();

        thread::sleep(tick_duration);
    }
    endwin();
}

fn get_index(x: usize, y: usize) -> usize {
    x * (WIDTH as usize) + y
}

fn draw_world(window: &Window, world: &Vec<u32>) {
    for x in 0..HEIGHT {
        window.mvaddstr(x, 0, "|");
        window.mvaddstr(x, WIDTH + 1, "|");
    }

    for y in 0..(WIDTH + 2) {
        window.mvaddstr(HEIGHT, y, "-");
    }

    for (i, item) in world.iter().enumerate() {
        if item > &0 {
            let index = i as i32;
            let x = index % WIDTH;
            let y = index / WIDTH;

            window.mvaddstr(y, x + 1, "#");
        }
    }
}

fn vec_of<T: Clone>(item: T, n: usize) -> Vec<T> {
    std::iter::repeat(item).take(n).collect()
}

#[derive(Display, EnumIter)]
enum Tetriminos {
    I,
    J,
    L,
    O,
    S,
    T,
    Z,
}
