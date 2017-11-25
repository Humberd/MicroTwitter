export interface ImgurImageUploadResponseDTO {
  status: number;
  success: boolean;
  data: ImgurImageUploadResponseData;
}

export interface ImgurImageUploadResponseData {
  link: string;

  [key: string]: any;
}
