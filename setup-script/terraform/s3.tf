resource "aws_s3_bucket" "popin-bucket" {
  bucket = "popin-bucket"

  tags = {
    name = "popin-bucket"
  }
}

resource "aws_s3_bucket_acl" "popin-bucket-acl" {
  bucket = aws_s3_bucket.popin-bucket.id
  acl    = "public-read"
}

resource "aws_s3_bucket_policy" "popin-bucket-policy" {
  bucket = aws_s3_bucket.popin-bucket.id

  policy = data.aws_iam_policy_document.bucket-policy.json
}

data "aws_iam_policy_document" "bucket-policy" {
  statement {
    actions = [
      "s3:GetObject"
    ]
    effect    = "Allow"
    resources = ["${aws_s3_bucket.popin-bucket.arn}/*"]
    principals {
      identifiers = ["*"]
      type        = "AWS"
    }
  }
}
