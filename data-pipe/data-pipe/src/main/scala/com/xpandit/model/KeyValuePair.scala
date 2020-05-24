package com.xpandit.model

import com.fasterxml.jackson.annotation.JsonProperty

case class KeyValuePair (@JsonProperty("name")name: String, @JsonProperty("value")value: Int) {

  def toTriple(): Triple = {
    Triple(name, value, value * 2)
  }
}
