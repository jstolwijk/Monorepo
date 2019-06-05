use actix_web::web::Json;
use actix_web::{web, HttpRequest};
use serde::Deserialize;
use std::borrow::Cow;

use super::user_model::User;
use super::user_service::hash_password;

use uuid::Uuid;

use crate::application_error::ApplicationError;
use validator::{Validate, ValidationErrors};

pub fn get_user_by_id(req: HttpRequest, user_id: web::Path<u32>) -> Json<User> {
  println!("REQ: {:?}", req);

  Json(User {
    id: user_id.into_inner(),
    uuid: Uuid::new_v4(),
    username: String::from("JesseStolwijk"),
    password: String::from("Welkom01"),
    active: true,
    monetary_accounts: Vec::new(),
  })
}

pub fn get_users(req: HttpRequest) -> String {
  String::from("Users")
}

#[derive(Deserialize, Debug, Validate)]
pub struct CreateUserRequestBody {
  #[validate(email(code = "mail", message = "username is an invalid email address"))]
  username: String,
  #[validate(length(min = "8", message = "password too short"))]
  #[validate(length(max = "1024", message = "password too long"))]
  password: String,
}

pub fn create_user(
  request_body: web::Json<CreateUserRequestBody>,
) -> Result<Json<User>, ApplicationError> {
  match request_body.validate() {
    Ok(_) => (),
    Err(e) => {
      return Err(ApplicationError::ValidationError {
        message: get_validation_error(e),
      });
    }
  };

  String::from("User created");

  Ok(Json(User {
    id: 1,
    uuid: Uuid::new_v4(),
    username: request_body.username.clone(),
    password: hash_password(&request_body.password),
    active: true,
    monetary_accounts: Vec::new(),
  }))
}

fn get_validation_error(validation_errors: ValidationErrors) -> Cow<'static, str> {
  // TODO handle unwrap

  validation_errors
    .field_errors()
    .values()
    .next()
    .unwrap()
    .first()
    .unwrap()
    .message
    .clone() // TODO remove clone
    .unwrap()
}
