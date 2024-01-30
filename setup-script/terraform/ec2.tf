resource "aws_security_group" "popin-server-sg" {
  name        = "popin server sg"
  description = "popin server sg"

  ingress {
    description = "SSH from VPC"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTP from VPC"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "popin-server" {
  ami           = "ami-0a3c3a20c09d6f377"
  instance_type = "t2.micro"
  key_name      = "pinpong"
  security_groups = [aws_security_group.popin-server-sg.name]

  tags = {
    Name = "popin app server"
  }
}
