use serde::{Deserialize, Serialize};
use uuid::Uuid;

#[derive(Deserialize, Serialize, Debug)]
pub struct User {
  #[serde(skip_serializing)]
  pub id: u32,
  pub uuid: Uuid,
  pub username: String,
  #[serde(skip_serializing)]
  pub password: String,
  pub active: bool,
  pub monetary_accounts: Vec<MonetaryAccount>, //might want to remove this here
}

#[derive(Deserialize, Serialize, Debug)]
pub struct MonetaryAccount {
  #[serde(skip_serializing)]
  pub id: u32,
  pub uuid: Uuid,
  pub name: String,
}
