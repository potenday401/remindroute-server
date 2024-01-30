resource "aws_s3_bucket" "potenday401-popin-bucket" {
  bucket = "potenday401-popin-bucket"

  tags = {
    name = "popin-bucket"
  }
}

resource "aws_s3_bucket_acl" "potenday401-popin-bucket-acl" {
  bucket = aws_s3_bucket.potenday401-popin-bucket.id
  acl    = "public-read"
  depends_on = [aws_s3_bucket_ownership_controls.potenday401-popin-bucket-acl-ownership]
}

resource "aws_s3_bucket_ownership_controls" "potenday401-popin-bucket-acl-ownership" {
  bucket = aws_s3_bucket.potenday401-popin-bucket.id
  rule {
    object_ownership = "BucketOwnerPreferred"
  }
  depends_on = [aws_s3_bucket_public_access_block.potenday401-popin-bucket-public-access]
}

resource "aws_s3_bucket_public_access_block" "potenday401-popin-bucket-public-access" {
  bucket = aws_s3_bucket.potenday401-popin-bucket.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_bucket_policy" "potenday401-popin-bucket-policy" {
  bucket = aws_s3_bucket.potenday401-popin-bucket.id

  policy = data.aws_iam_policy_document.bucket-policy.json
  depends_on = [aws_s3_bucket_public_access_block.potenday401-popin-bucket-public-access]
}

data "aws_iam_policy_document" "bucket-policy" {
  statement {
    actions = [
      "s3:GetObject"
    ]
    effect    = "Allow"
    resources = ["${aws_s3_bucket.potenday401-popin-bucket.arn}/*"]
    principals {
      identifiers = ["*"]
      type        = "AWS"
    }
  }
}
