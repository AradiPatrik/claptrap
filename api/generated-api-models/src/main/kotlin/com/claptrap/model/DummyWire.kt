/**
* Claptrap
* No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
*
* The version of the OpenAPI document: 1.0
* 
*
* NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
* https://openapi-generator.tech
* Do not edit the class manually.
*/
package com.claptrap.model


import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 * @param id 
 * @param name 
 * @param kaga 
 * @param kugi 
 */

data class DummyWire (
    @field:JsonProperty("id")
    val id: kotlin.Int,
    @field:JsonProperty("name")
    val name: kotlin.String,
    @field:JsonProperty("kaga")
    val kaga: kotlin.String? = null,
    @field:JsonProperty("kugi")
    val kugi: kotlin.String? = null
)

