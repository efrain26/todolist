package org.efradev.todolist.data.mapper

import org.efradev.todolist.data.model.ShoppingList
import org.efradev.todolist.data.model.ShoppingItem
import org.efradev.todolist.data.model.CreateShoppingListRequest
import org.efradev.todolist.data.model.AddItemRequest
import org.efradev.todolist.domain.model.DomainShoppingList
import org.efradev.todolist.domain.model.DomainShoppingItem
import org.efradev.todolist.domain.model.DomainAddItemRequest

/**
 * Mappers for converting between Shopping-related Data DTOs and Domain models
 * 
 * These mappers handle the conversion between data representation (API DTOs)
 * and domain representation (business objects).
 */

// ShoppingList (Data) → DomainShoppingList (Domain)
fun ShoppingList.toDomain(): DomainShoppingList = DomainShoppingList(
    id = id,
    name = name,
    createdAt = createdAt,
    userId = userId,
    type = type,
    items = items.map { it.toDomain() }
)

// ShoppingItem (Data) → DomainShoppingItem (Domain)
fun ShoppingItem.toDomain(): DomainShoppingItem = DomainShoppingItem(
    name = name,
    status = status,
    type = type
)

// Domain create request → Data create request
fun createShoppingListRequest(name: String, type: String): CreateShoppingListRequest = 
    CreateShoppingListRequest(
        name = name,
        type = type
    )

// DomainAddItemRequest (Domain) → AddItemRequest (Data)
fun DomainAddItemRequest.toData(): AddItemRequest = AddItemRequest(
    name = name,
    type = listType,
    price = price,
    quantity = quantity,
    url = url,
    store = store,
    notes = notes,
    platform = platform,
    genre = genre,
    year = year,
    rating = rating,
    dueDate = dueDate,
    priority = priority
)
