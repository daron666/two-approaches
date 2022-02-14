import zio.Has

package object first {

  type CacheService    = Has[Cache.Service]
  type DatabaseService = Has[Database.Service]
  type ApiService      = Has[Api.Service]
}
