/**
 * @file Pair.java
 * @author gwerry
 * @brief The Pair class represents a generic pair of two objects.
 * @version 1.0
 * @date 2024/01/03
 *
 * Copyright 2024 gwerry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gwerry;

/**
 * @brief The Pair class represents a generic pair of two objects.
 * It holds two public members: first and second, which can be of any type.
 * This class can be used when you want to return or handle two objects together.
 *
 * @param <T1> the type of the first object in the pair
 * @param <T2> the type of the second object in the pair
 *
 * @author gwerry
 * @since 1.0
 */
public class Pair<T1, T2> {
    public T1 first;
    public T2 second;

    /**
     * @brief Constructs a new Pair with the given values.
     *
     * @param first the first value
     * @param second the second value
     */
    public Pair(T1 first, T2 second) {
        this.first  = first;
        this.second = second;
    }
}