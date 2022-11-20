/// <reference types="react-scripts" />
declare namespace NodeJS {
  export interface ProcessEnv {
    REACT_APP_CRYPTO_SECRET_KEY: string;
    REACT_APP_CRYPTO_IV: string;
  }
}
