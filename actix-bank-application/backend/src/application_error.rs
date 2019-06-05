use actix_web::{error, http, web, HttpRequest, HttpResponse};
use std::borrow::Cow;

#[derive(Fail, Debug)]
pub enum ApplicationError {
  #[fail(display = "internal error")]
  InternalError,
  #[fail(display = "bad request")]
  Unauthorized,
  #[fail(display = "timeout")]
  Timeout,
  #[fail(display = "validation error: {}", message)]
  ValidationError { message: Cow<'static, str> },
}

impl error::ResponseError for ApplicationError {
  fn error_response(&self) -> HttpResponse {
    match *self {
      ApplicationError::InternalError => HttpResponse::new(http::StatusCode::INTERNAL_SERVER_ERROR),
      ApplicationError::Unauthorized => HttpResponse::new(http::StatusCode::UNAUTHORIZED),
      ApplicationError::Timeout => HttpResponse::new(http::StatusCode::GATEWAY_TIMEOUT),
      ApplicationError::ValidationError { message: _ } => {
        HttpResponse::new(http::StatusCode::BAD_REQUEST)
      }
    }
  }
}
