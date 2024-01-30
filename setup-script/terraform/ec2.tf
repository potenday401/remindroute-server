resource "aws_instance" "app_server" {
  ami           = "ami-0a3c3a20c09d6f377"
  instance_type = "t2.micro"
  key_name      = "pinpong"

  tags = {
    Name = "popin app server"
  }
}