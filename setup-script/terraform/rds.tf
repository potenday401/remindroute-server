resource "aws_security_group" "popin-rds-sg" {
  name        = "popin rds security group"
  description = "popin rds security group"
  tags = {
    name = "popin-rds-sg"
  }

  ingress {
    protocol    = "tcp"
    from_port   = 3306
    to_port     = 3306
    cidr_blocks = ["0.0.0.0/0"]
    # ec2 에서 접근
    security_groups = [aws_security_group.popin-server-sg.id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}


resource "aws_db_instance" "popin-rds" {
  allocated_storage = 50
  identifier        = "popin-rds"
  storage_type      = "gp2"
  engine            = "mysql"
  engine_version    = "8.0.28"
  instance_class    = "db.t2.micro"
  db_name           = "remindroute"
  username          = "root"
  password          = "mysqlrootpw"
  # db를 삭제시 아래 주석 활성
  # skip_final_snapshot = true

  vpc_security_group_ids = [
    aws_security_group.popin-rds-sg.id,
  ]
  tags = {
    "name" = "popin rds"
  }
}