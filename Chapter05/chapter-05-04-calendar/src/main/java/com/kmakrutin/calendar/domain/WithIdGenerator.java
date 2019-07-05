package com.kmakrutin.calendar.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class WithIdGenerator
{
  private static final AtomicInteger ID_GENERATOR = new AtomicInteger( 1 );

  public static Integer nextId()
  {
    return ID_GENERATOR.incrementAndGet();
  }
}
