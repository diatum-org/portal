/**
 * Device Portal API
 * Portal for registering and maintaining digital identity nodes emigo-id - hex encoded sha256 of an emigo 4096 public key secure-token - hex encoded 256 bit random number
 *
 * OpenAPI spec version: 1.0.4
 * Contact: rolandosborne@diatum.org
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

export interface IdentityToken { 
    accountToken?: string;
    accessToken?: string;
    statToken?: string;
    confToken?: string;
}