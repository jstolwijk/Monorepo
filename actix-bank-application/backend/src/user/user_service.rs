extern crate bcrypt;

use bcrypt::{hash, verify, DEFAULT_COST};

use super::user_model::User;
use uuid::Uuid;

pub fn hash_password(password: &str) -> String {
  hash(password, DEFAULT_COST).unwrap()
}

pub fn verify_password(password: String, hash_password: String) -> bool {
  verify(password, &hash_password).unwrap()
}

pub fn create_user(username: &str, password: &str) -> User {
  let hashed_password = hash_password(password);
  User {
    id: 1,
    uuid: Uuid::new_v4(),
    username: String::from(username),
    password: hashed_password,
    active: true,
    monetary_accounts: Vec::new(),
  }
}
