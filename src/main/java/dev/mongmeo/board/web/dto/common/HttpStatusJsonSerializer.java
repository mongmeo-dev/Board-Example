package dev.mongmeo.board.web.dto.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.springframework.http.HttpStatus;

public class HttpStatusJsonSerializer extends StdSerializer<HttpStatus> {

  public HttpStatusJsonSerializer() {
    super(HttpStatus.class);
  }

  protected HttpStatusJsonSerializer(Class<HttpStatus> t) {
    super(t);
  }

  @Override
  public void serialize(HttpStatus value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeNumber(value.value());
  }
}
