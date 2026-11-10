import { useCallback } from 'react'
import { useApi } from '.././common/useApi'
import * as ordersApi from '../../api/orders'
import type { CreateOrderRequest, OrderResponse } from '../../types'

export function useOrders() {
  const createApi = useApi<OrderResponse>()
  const { execute } = createApi

  const createOrder = useCallback((data: CreateOrderRequest) => {
    return execute(() => ordersApi.createOrder(data))
  }, [execute])

  return {
    createOrder,
    order: createApi.data,
    loading: createApi.loading,
    error: createApi.error
  }
}