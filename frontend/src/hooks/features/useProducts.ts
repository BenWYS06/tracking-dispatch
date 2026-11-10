import { useState, useCallback } from 'react'
import { useApi } from '.././common/useApi'
import * as productsApi from '../../api/products'
import type { ProductListResponse, ProductResponse } from '../../types'
import type { ProductFilters } from '../../types/product'
import type { Page } from '../../types/common'

export function useProducts() {
  const listApi = useApi<Page<ProductListResponse>>()
  const { execute: executeList } = listApi
  const itemApi = useApi<ProductResponse>()
  const { execute: executeItem } = itemApi
  const { execute: executePopular } = useApi<ProductListResponse[]>()
  const { execute: executeRelated } = useApi<ProductListResponse[]>()
  const [products, setProducts] = useState<ProductListResponse[]>([])
  const [popular, setPopular] = useState<ProductListResponse[]>([])
  const [related, setRelated] = useState<ProductListResponse[]>([])

  const loadProducts = useCallback(async (page = 0, filters?: ProductFilters) => {
    const data = await executeList(() => productsApi.getProducts(page, 12, filters))
    setProducts(data.content)
  }, [executeList])

  const loadMoreProducts = useCallback(async (page = 0, filters?: ProductFilters) => {
    const data = await executeList(() => productsApi.getProducts(page, 12, filters))
    setProducts(prev => page === 0 ? data.content : [...prev, ...data.content])
  }, [executeList])

  const loadPopular = useCallback(async () => {
    const data = await executePopular(() => productsApi.getPopularProducts())
    if (data) setPopular(data)
  }, [executePopular])

  const loadRelated = useCallback(async (id: number) => {
    const data = await executeRelated(() => productsApi.getRelatedProducts(id))
    if (data) setRelated(data)
  }, [executeRelated])

  const getProduct = useCallback(
    (id: number) => executeItem(() => productsApi.getProduct(id)),
    [executeItem]
  )

  const getProductBySlug = useCallback(
    (slug: string) => executeItem(() => productsApi.getProductBySlug(slug)),
    [executeItem]
  )

  return {
    products,
    popular,
    related,
    totalPages: listApi.data?.page.totalPages || 0,
    loading: listApi.loading,
    error: listApi.error,
    loadProducts,
    loadMoreProducts,
    loadPopular,
    loadRelated,
    getProduct,
    getProductBySlug,
    product: itemApi.data,
    productLoading: itemApi.loading
  }
}
