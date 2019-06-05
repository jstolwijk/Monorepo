use chrono::{Duration, Local};
use jsonwebtoken::{decode, encode, Algorithm, Header, Validation};
#[derive(Debug, Serialize, Deserialize)]
struct Claims {
  sub: String,
  exp: i64,
}

pub fn create_jwt(username: String) -> String {
  let expiry_moment = Local::now() + Duration::minutes(5);

  let my_claims = Claims {
    sub: username,
    exp: expiry_moment.timestamp(),
  };

  //TODO error handling
  encode(&Header::default(), &my_claims, "secret".as_ref()).unwrap()
}

pub fn validate_jwt(jwt: &str) -> bool {
  let claims = decode::<Claims>(jwt, "secret".as_ref(), &Validation::new(Algorithm::HS256));
  println!("{:?}", claims);
  match claims {
    Ok(_) => true,
    Err(_) => false,
  }
}

#[cfg(test)]
mod tests {
  use super::*;

  #[test]
  fn test_create_jwt() {
    let jwt = create_jwt(String::from("jesse"));

    let actual = validate_jwt(&jwt);
    assert_eq!(true, actual);
  }
}
