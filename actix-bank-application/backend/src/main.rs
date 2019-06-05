#[macro_use]
extern crate serde_derive;
#[macro_use]
extern crate validator_derive;
#[macro_use]
extern crate failure;
#[macro_use]
extern crate log;

use actix_web::middleware::{DefaultHeaders, Logger};
use actix_web::{http, web, App, HttpRequest, HttpResponse, HttpServer};

mod user;
use user::{user_api, user_service};

mod application_error;
mod authentication;

pub struct AppState {}

fn get_monetary_account(
    req: HttpRequest,
    path: web::Path<(String, String)>, // account_id: web::Path<String>
) -> String {
    println!("REQ: {:?}", req);
    format!("get_user_account: {} {}!\r\n", path.0, path.1)
}

fn index() -> &'static str {
    "Hello world!\r\n"
}

#[derive(Deserialize, Debug)]
pub struct CreateUserRequestBody {
    username: String,
    password: String,
}

fn join(request_body: web::Json<CreateUserRequestBody>) -> HttpResponse {
    let user = user_service::create_user(&request_body.username, &request_body.password);

    let jwt = authentication::jwt_service::create_jwt(user.username);

    HttpResponse::build(http::StatusCode::OK)
        .header("X-AUTH-TOKEN", jwt)
        .finish()
}

fn main() {
    // https://actix.rs/actix-web/actix_web/middleware/struct.Logger.html
    std::env::set_var("RUST_LOG", "actix_web=info");
    env_logger::init();

    let _ = HttpServer::new(|| {
        App::new()
            .wrap(Logger::default())
            .wrap(DefaultHeaders::new().header("X-Version", "1.0"))
            // .wrap(encoding::Compress::default())
            .service(
                web::scope("/api/v1")
                    .service(web::resource("/join").route(web::post().to(join)))
                    .service(web::resource("/authenticate").route(web::get().to(index)))
                    .service(
                        web::scope("/user")
                            .wrap(authentication::authentication_middleware::Auth)
                            .wrap(DefaultHeaders::new().header("X-Version-R2", "0.3"))
                            // .middleware(authentication::authentication_middleware::AuthMiddleware)
                            .service(
                                web::resource("")
                                    .route(web::get().to(user_api::get_users))
                                    .route(web::post().to(user_api::create_user)),
                            )
                            .service(
                                web::scope("/{user_id}")
                                    .service(
                                        web::resource("")
                                            .route(web::get().to(user_api::get_user_by_id)),
                                    )
                                    .service(
                                        web::resource("/monetary-account/{monetary_account_id}")
                                            .route(web::get().to(get_monetary_account)),
                                    ),
                            ),
                    ),
            )
        // .default_resource(|| HttpResponse::NotFound())
    })
    .bind("127.0.0.1:8088")
    .unwrap()
    .run();
}
