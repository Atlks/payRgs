package org.chwin.firefighting.apiserver.test;

import javassist.bytecode.stackmap.BasicBlock;
import org.hibernate.*;
import org.hibernate.MappingException;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.boot.*;
import org.hibernate.boot.archive.scan.spi.ScanEnvironment;
import org.hibernate.boot.archive.scan.spi.ScanOptions;
import org.hibernate.boot.archive.spi.ArchiveDescriptorFactory;
import org.hibernate.boot.internal.BootstrapContextImpl;
import org.hibernate.boot.internal.ClassmateContext;
import org.hibernate.boot.internal.MetadataImpl;
import org.hibernate.boot.model.IdGeneratorStrategyInterpreter;
import org.hibernate.boot.model.IdentifierGeneratorDefinition;
import org.hibernate.boot.model.TypeDefinition;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.relational.AuxiliaryDatabaseObject;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.*;
import org.hibernate.cache.spi.CacheImplementor;
import org.hibernate.cache.spi.TimestampsCacheFactory;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.BaselineSessionEventsListenerBuilder;
import org.hibernate.cfg.MetadataSourceType;
import org.hibernate.cfg.Settings;
import org.hibernate.cfg.annotations.NamedEntityGraphDefinition;
import org.hibernate.cfg.annotations.NamedProcedureCallDefinition;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.SQLFunctionRegistry;
import org.hibernate.engine.ResultSetMappingDefinition;
import org.hibernate.engine.jdbc.LobCreationContext;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.hibernate.engine.jdbc.env.spi.*;
import org.hibernate.engine.jdbc.spi.*;
import org.hibernate.engine.query.spi.QueryPlanCache;
import org.hibernate.engine.spi.*;
import org.hibernate.hql.spi.id.MultiTableBulkIdStrategy;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.spi.JpaCompliance;
import org.hibernate.jpa.spi.MutableJpaCompliance;
import org.hibernate.loader.BatchFetchStyle;
import org.hibernate.mapping.FetchProfile;
import org.hibernate.mapping.MappedSuperclass;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.hibernate.query.spi.NamedQueryRepository;
import org.hibernate.resource.jdbc.spi.PhysicalConnectionHandlingMode;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.hibernate.service.Service;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.stat.spi.StatisticsImplementor;
import org.hibernate.tuple.entity.EntityTuplizerFactory;
import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;
import org.hibernate.type.spi.TypeConfiguration;
import org.jboss.jandex.IndexView;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.persistence.*;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import java.sql.Connection;
import java.util.*;

public class SessionFactoryImplAtiimp  extends SessionFactoryDelegatingImpl implements SessionFactoryImplementor {

//    public    SessionFactoryImplAtiimp()
//    {
//n
//         //   super();
//
//    }
    public SessionFactoryImplAtiimp(SessionFactoryImplementor delegate) {
        super(delegate);
    }

    public static SessionFactoryImplementor newSessionFactoryImplAti() {
        BootstrapContext BootstrapContext1=   new BootstrapContext(){
            @Override
            public StandardServiceRegistry getServiceRegistry() {
                return new StandardServiceRegistry(){
                    @Override
                    public ServiceRegistry getParentServiceRegistry() {
                        return null;
                    }

                    @Override
                    public <R extends Service> R getService(Class<R> aClass) {
                        return null;
                    }

                    @Override
                    public void close() {

                    }
                };
            }

            @Override
            public MutableJpaCompliance getJpaCompliance() {
                return null;
            }

            @Override
            public TypeConfiguration getTypeConfiguration() {
                return null;
            }

            @Override
            public MetadataBuildingOptions getMetadataBuildingOptions() {
                return null;
            }

            @Override
            public boolean isJpaBootstrap() {
                return false;
            }

            @Override
            public void markAsJpaBootstrap() {

            }

            @Override
            public ClassLoader getJpaTempClassLoader() {
                return null;
            }

            @Override
            public ClassLoaderAccess getClassLoaderAccess() {
                return null;
            }

            @Override
            public ClassmateContext getClassmateContext() {
                return null;
            }

            @Override
            public ArchiveDescriptorFactory getArchiveDescriptorFactory() {
                return null;
            }

            @Override
            public ScanOptions getScanOptions() {
                return null;
            }

            @Override
            public ScanEnvironment getScanEnvironment() {
                return null;
            }

            @Override
            public Object getScanner() {
                return null;
            }

            @Override
            public ReflectionManager getReflectionManager() {
                return null;
            }

            @Override
            public IndexView getJandexView() {
                return null;
            }

            @Override
            public Map<String, SQLFunction> getSqlFunctions() {
                return null;
            }

            @Override
            public Collection<AuxiliaryDatabaseObject> getAuxiliaryDatabaseObjectList() {
                return null;
            }

            @Override
            public Collection<AttributeConverterInfo> getAttributeConverters() {
                return null;
            }

            @Override
            public Collection<CacheRegionDefinition> getCacheRegionDefinitions() {
                return null;
            }

            @Override
            public void release() {

            }
        };
        Metadata Metadata1=new Metadata(){
            @Override
            public IdentifierGeneratorFactory getIdentifierGeneratorFactory() {
                return null;
            }

            @Override
            public Type getIdentifierType(String s) throws MappingException {
                return null;
            }

            @Override
            public String getIdentifierPropertyName(String s) throws MappingException {
                return null;
            }

            @Override
            public Type getReferencedPropertyType(String s, String s1) throws MappingException {
                return null;
            }

            @Override
            public SessionFactoryBuilder getSessionFactoryBuilder() {
                return null;
            }

            @Override
            public SessionFactory buildSessionFactory() {
                return null;
            }

            @Override
            public UUID getUUID() {
                return null;
            }

            @Override
            public Database getDatabase() {
                return null;
            }

            @Override
            public Collection<PersistentClass> getEntityBindings() {
                return null;
            }

            @Override
            public PersistentClass getEntityBinding(String s) {
                return null;
            }

            @Override
            public Collection<org.hibernate.mapping.Collection> getCollectionBindings() {
                return null;
            }

            @Override
            public org.hibernate.mapping.Collection getCollectionBinding(String s) {
                return null;
            }

            @Override
            public Map<String, String> getImports() {
                return null;
            }

            @Override
            public NamedQueryDefinition getNamedQueryDefinition(String s) {
                return null;
            }

            @Override
            public Collection<NamedQueryDefinition> getNamedQueryDefinitions() {
                return null;
            }

            @Override
            public NamedSQLQueryDefinition getNamedNativeQueryDefinition(String s) {
                return null;
            }

            @Override
            public Collection<NamedSQLQueryDefinition> getNamedNativeQueryDefinitions() {
                return null;
            }

            @Override
            public Collection<NamedProcedureCallDefinition> getNamedProcedureCallDefinitions() {
                return null;
            }

            @Override
            public ResultSetMappingDefinition getResultSetMapping(String s) {
                return null;
            }

            @Override
            public Map<String, ResultSetMappingDefinition> getResultSetMappingDefinitions() {
                return null;
            }

            @Override
            public TypeDefinition getTypeDefinition(String s) {
                return null;
            }

            @Override
            public Map<String, FilterDefinition> getFilterDefinitions() {
                return null;
            }

            @Override
            public FilterDefinition getFilterDefinition(String s) {
                return null;
            }

            @Override
            public FetchProfile getFetchProfile(String s) {
                return null;
            }

            @Override
            public Collection<FetchProfile> getFetchProfiles() {
                return null;
            }

            @Override
            public NamedEntityGraphDefinition getNamedEntityGraph(String s) {
                return null;
            }

            @Override
            public Map<String, NamedEntityGraphDefinition> getNamedEntityGraphs() {
                return null;
            }

            @Override
            public IdentifierGeneratorDefinition getIdentifierGenerator(String s) {
                return null;
            }

            @Override
            public Collection<Table> collectTableMappings() {
                return null;
            }

            @Override
            public Map<String, SQLFunction> getSqlFunctionMap() {
                return null;
            }
        };
        SessionFactoryOptions SessionFactoryOptions1= new SessionFactoryOptions() {
            @Override
            public String getUuid() {
                return null;
            }

            @Override
            public StandardServiceRegistry getServiceRegistry() {
                return null;
            }

            @Override
            public Object getBeanManagerReference() {
                return null;
            }

            @Override
            public Object getValidatorFactoryReference() {
                return null;
            }

            @Override
            public boolean isJpaBootstrap() {
                return false;
            }

            @Override
            public boolean isJtaTransactionAccessEnabled() {
                return false;
            }

            @Override
            public String getSessionFactoryName() {
                return null;
            }

            @Override
            public boolean isSessionFactoryNameAlsoJndiName() {
                return false;
            }

            @Override
            public boolean isFlushBeforeCompletionEnabled() {
                return false;
            }

            @Override
            public boolean isAutoCloseSessionEnabled() {
                return false;
            }

            @Override
            public boolean isStatisticsEnabled() {
                return false;
            }

            @Override
            public Interceptor getInterceptor() {
                return null;
            }

            @Override
            public Class<? extends Interceptor> getStatelessInterceptorImplementor() {
                return null;
            }

            @Override
            public StatementInspector getStatementInspector() {
                return null;
            }

            @Override
            public SessionFactoryObserver[] getSessionFactoryObservers() {
                return new SessionFactoryObserver[0];
            }

            @Override
            public BaselineSessionEventsListenerBuilder getBaselineSessionEventsListenerBuilder() {
                return null;
            }

            @Override
            public boolean isIdentifierRollbackEnabled() {
                return false;
            }

            @Override
            public EntityMode getDefaultEntityMode() {
                return null;
            }

            @Override
            public EntityTuplizerFactory getEntityTuplizerFactory() {
                return null;
            }

            @Override
            public boolean isCheckNullability() {
                return false;
            }

            @Override
            public boolean isInitializeLazyStateOutsideTransactionsEnabled() {
                return false;
            }

            @Override
            public MultiTableBulkIdStrategy getMultiTableBulkIdStrategy() {
                return null;
            }

            @Override
            public TempTableDdlTransactionHandling getTempTableDdlTransactionHandling() {
                return null;
            }

            @Override
            public BatchFetchStyle getBatchFetchStyle() {
                return null;
            }

            @Override
            public boolean isDelayBatchFetchLoaderCreationsEnabled() {
                return false;
            }

            @Override
            public int getDefaultBatchFetchSize() {
                return 0;
            }

            @Override
            public Integer getMaximumFetchDepth() {
                return null;
            }

            @Override
            public NullPrecedence getDefaultNullPrecedence() {
                return null;
            }

            @Override
            public boolean isOrderUpdatesEnabled() {
                return false;
            }

            @Override
            public boolean isOrderInsertsEnabled() {
                return false;
            }

            @Override
            public MultiTenancyStrategy getMultiTenancyStrategy() {
                return null;
            }

            @Override
            public CurrentTenantIdentifierResolver getCurrentTenantIdentifierResolver() {
                return null;
            }

            @Override
            public boolean isJtaTrackByThread() {
                return false;
            }

            @Override
            public Map getQuerySubstitutions() {
                return null;
            }

            @Override
            public boolean isNamedQueryStartupCheckingEnabled() {
                return false;
            }

            @Override
            public boolean isConventionalJavaConstants() {
                return false;
            }

            @Override
            public boolean isSecondLevelCacheEnabled() {
                return false;
            }

            @Override
            public boolean isQueryCacheEnabled() {
                return false;
            }

            @Override
            public TimestampsCacheFactory getTimestampsCacheFactory() {
                return null;
            }

            @Override
            public String getCacheRegionPrefix() {
                return null;
            }

            @Override
            public boolean isMinimalPutsEnabled() {
                return false;
            }

            @Override
            public boolean isStructuredCacheEntriesEnabled() {
                return false;
            }

            @Override
            public boolean isDirectReferenceCacheEntriesEnabled() {
                return false;
            }

            @Override
            public boolean isAutoEvictCollectionCache() {
                return false;
            }

            @Override
            public SchemaAutoTooling getSchemaAutoTooling() {
                return null;
            }

            @Override
            public int getJdbcBatchSize() {
                return 0;
            }

            @Override
            public boolean isJdbcBatchVersionedData() {
                return false;
            }

            @Override
            public boolean isScrollableResultSetsEnabled() {
                return false;
            }

            @Override
            public boolean isWrapResultSetsEnabled() {
                return false;
            }

            @Override
            public boolean isGetGeneratedKeysEnabled() {
                return false;
            }

            @Override
            public Integer getJdbcFetchSize() {
                return null;
            }

            @Override
            public PhysicalConnectionHandlingMode getPhysicalConnectionHandlingMode() {
                return null;
            }

            @Override
            public ConnectionReleaseMode getConnectionReleaseMode() {
                return null;
            }

            @Override
            public boolean isCommentsEnabled() {
                return false;
            }

            @Override
            public CustomEntityDirtinessStrategy getCustomEntityDirtinessStrategy() {
                return null;
            }

            @Override
            public EntityNameResolver[] getEntityNameResolvers() {
                return new EntityNameResolver[0];
            }

            @Override
            public EntityNotFoundDelegate getEntityNotFoundDelegate() {
                return null;
            }

            @Override
            public Map<String, SQLFunction> getCustomSqlFunctionMap() {
                return null;
            }

            @Override
            public void setCheckNullability(boolean b) {

            }

            @Override
            public boolean isPreferUserTransaction() {
                return false;
            }

            @Override
            public boolean isProcedureParameterNullPassingEnabled() {
                return false;
            }

            @Override
            public boolean isCollectionJoinSubqueryRewriteEnabled() {
                return false;
            }

            @Override
            public boolean isAllowOutOfTransactionUpdateOperations() {
                return false;
            }

            @Override
            public boolean isReleaseResourcesOnCloseEnabled() {
                return false;
            }

            @Override
            public TimeZone getJdbcTimeZone() {
                return null;
            }

            @Override
            public boolean jdbcStyleParamsZeroBased() {
                return false;
            }

            @Override
            public JpaCompliance getJpaCompliance() {
                return null;
            }

            @Override
            public boolean isFailOnPaginationOverCollectionFetchEnabled() {
                return false;
            }
        };
        MetadataImplementor MetadataImplementor1=new MetadataImplementor(){
            @Override
            public IdentifierGeneratorFactory getIdentifierGeneratorFactory() {
                return null;
            }

            @Override
            public Type getIdentifierType(String s) throws MappingException {
                return null;
            }

            @Override
            public String getIdentifierPropertyName(String s) throws MappingException {
                return null;
            }

            @Override
            public Type getReferencedPropertyType(String s, String s1) throws MappingException {
                return null;
            }

            @Override
            public SessionFactoryBuilder getSessionFactoryBuilder() {
                return null;
            }

            @Override
            public SessionFactory buildSessionFactory() {
                return null;
            }

            @Override
            public UUID getUUID() {
                return null;
            }

            @Override
            public Database getDatabase() {
                return new Database(new MetadataBuildingOptions(){
                    @Override
                    public StandardServiceRegistry getServiceRegistry() {
                        return new StandardServiceRegistry(){
                            @Override
                            public ServiceRegistry getParentServiceRegistry() {
                                return null;
                            }

                            @Override
                            public <R extends Service> R getService(Class<R> aClass) {
                            //    if(  R instanceof JdbcEnvironment )

                                if(aClass.getName().equals("org.hibernate.engine.jdbc.spi.JdbcServices"))
                                {
                                    return (R) new JdbcServices() {
                                        @Override
                                        public JdbcEnvironment getJdbcEnvironment() {
                                            return null;
                                        }

                                        @Override
                                        public JdbcConnectionAccess getBootstrapJdbcConnectionAccess() {
                                            return null;
                                        }

                                        @Override
                                        public Dialect getDialect() {
                                            return null;
                                        }

                                        @Override
                                        public SqlStatementLogger getSqlStatementLogger() {
                                            return null;
                                        }

                                        @Override
                                        public SqlExceptionHelper getSqlExceptionHelper() {
                                            return null;
                                        }

                                        @Override
                                        public ExtractedDatabaseMetaData getExtractedMetaDataSupport() {
                                            return null;
                                        }

                                        @Override
                                        public LobCreator getLobCreator(LobCreationContext lobCreationContext) {
                                            return null;
                                        }

                                        @Override
                                        public ResultSetWrapper getResultSetWrapper() {
                                            return null;
                                        }
                                    };
                                }
                                if(aClass.getName().equals("org.hibernate.engine.jdbc.spi.JdbcEnvironment"))
                                {

                                    return (R) new JdbcEnvironment() {
                                        @Override
                                        public Dialect getDialect() {
                                            return null;
                                        }

                                        @Override
                                        public ExtractedDatabaseMetaData getExtractedDatabaseMetaData() {
                                            return null;
                                        }

                                        @Override
                                        public Identifier getCurrentCatalog() {
                                            return null;
                                        }

                                        @Override
                                        public Identifier getCurrentSchema() {
                                            return null;
                                        }

                                        @Override
                                        public QualifiedObjectNameFormatter getQualifiedObjectNameFormatter() {
                                            return null;
                                        }

                                        @Override
                                        public IdentifierHelper getIdentifierHelper() {
                                            return null;
                                        }

                                        @Override
                                        public NameQualifierSupport getNameQualifierSupport() {
                                            return null;
                                        }

                                        @Override
                                        public SqlExceptionHelper getSqlExceptionHelper() {
                                            return null;
                                        }

                                        @Override
                                        public LobCreatorBuilder getLobCreatorBuilder() {
                                            return null;
                                        }

                                        @Override
                                        public TypeInfo getTypeInfoForJdbcCode(int i) {
                                            return null;
                                        }
                                    };
                                }


                                return (R) new JdbcEnvironment() {
                                    @Override
                                    public Dialect getDialect() {
                                        return null;
                                    }

                                    @Override
                                    public ExtractedDatabaseMetaData getExtractedDatabaseMetaData() {
                                        return null;
                                    }

                                    @Override
                                    public Identifier getCurrentCatalog() {
                                        return null;
                                    }

                                    @Override
                                    public Identifier getCurrentSchema() {
                                        return null;
                                    }

                                    @Override
                                    public QualifiedObjectNameFormatter getQualifiedObjectNameFormatter() {
                                        return null;
                                    }

                                    @Override
                                    public IdentifierHelper getIdentifierHelper() {
                                        return null;
                                    }

                                    @Override
                                    public NameQualifierSupport getNameQualifierSupport() {
                                        return null;
                                    }

                                    @Override
                                    public SqlExceptionHelper getSqlExceptionHelper() {
                                        return null;
                                    }

                                    @Override
                                    public LobCreatorBuilder getLobCreatorBuilder() {
                                        return null;
                                    }

                                    @Override
                                    public TypeInfo getTypeInfoForJdbcCode(int i) {
                                        return null;
                                    }
                                };
                                 //   aClass instanceof  JdbcEnvironment
                                    //aClass instanceof JdbcEnvironment.class


                            }

                            @Override
                            public void close() {

                            }
                        };
                    }

                    @Override
                    public MappingDefaults getMappingDefaults() {
                        return new MappingDefaults(){
                            @Override
                            public String getImplicitSchemaName() {
                                return null;
                            }

                            @Override
                            public String getImplicitCatalogName() {
                                return null;
                            }

                            @Override
                            public boolean shouldImplicitlyQuoteIdentifiers() {
                                return false;
                            }

                            @Override
                            public String getImplicitIdColumnName() {
                                return null;
                            }

                            @Override
                            public String getImplicitTenantIdColumnName() {
                                return null;
                            }

                            @Override
                            public String getImplicitDiscriminatorColumnName() {
                                return null;
                            }

                            @Override
                            public String getImplicitPackageName() {
                                return null;
                            }

                            @Override
                            public boolean isAutoImportEnabled() {
                                return false;
                            }

                            @Override
                            public String getImplicitCascadeStyleName() {
                                return null;
                            }

                            @Override
                            public String getImplicitPropertyAccessorName() {
                                return null;
                            }

                            @Override
                            public boolean areEntitiesImplicitlyLazy() {
                                return false;
                            }

                            @Override
                            public boolean areCollectionsImplicitlyLazy() {
                                return false;
                            }

                            @Override
                            public AccessType getImplicitCacheAccessType() {
                                return null;
                            }
                        };
                    }

                    @Override
                    public List<BasicTypeRegistration> getBasicTypeRegistrations() {
                        return null;
                    }

                    @Override
                    public ReflectionManager getReflectionManager() {
                        return null;
                    }

                    @Override
                    public IndexView getJandexView() {
                        return null;
                    }

                    @Override
                    public ScanOptions getScanOptions() {
                        return null;
                    }

                    @Override
                    public ScanEnvironment getScanEnvironment() {
                        return null;
                    }

                    @Override
                    public Object getScanner() {
                        return null;
                    }

                    @Override
                    public ArchiveDescriptorFactory getArchiveDescriptorFactory() {
                        return null;
                    }

                    @Override
                    public ClassLoader getTempClassLoader() {
                        return null;
                    }

                    @Override
                    public ImplicitNamingStrategy getImplicitNamingStrategy() {
                        return null;
                    }

                    @Override
                    public PhysicalNamingStrategy getPhysicalNamingStrategy() {
                        return null;
                    }

                    @Override
                    public SharedCacheMode getSharedCacheMode() {
                        return null;
                    }

                    @Override
                    public AccessType getImplicitCacheAccessType() {
                        return null;
                    }

                    @Override
                    public MultiTenancyStrategy getMultiTenancyStrategy() {
                        return null;
                    }

                    @Override
                    public IdGeneratorStrategyInterpreter getIdGenerationTypeInterpreter() {
                        return null;
                    }

                    @Override
                    public List<CacheRegionDefinition> getCacheRegionDefinitions() {
                        return null;
                    }

                    @Override
                    public boolean ignoreExplicitDiscriminatorsForJoinedInheritance() {
                        return false;
                    }

                    @Override
                    public boolean createImplicitDiscriminatorsForJoinedInheritance() {
                        return false;
                    }

                    @Override
                    public boolean shouldImplicitlyForceDiscriminatorInSelect() {
                        return false;
                    }

                    @Override
                    public boolean useNationalizedCharacterData() {
                        return false;
                    }

                    @Override
                    public boolean isSpecjProprietarySyntaxEnabled() {
                        return false;
                    }

                    @Override
                    public List<MetadataSourceType> getSourceProcessOrdering() {
                        return null;
                    }

                    @Override
                    public Map<String, SQLFunction> getSqlFunctions() {
                        return null;
                    }

                    @Override
                    public List<AuxiliaryDatabaseObject> getAuxiliaryDatabaseObjectList() {
                        return null;
                    }

                    @Override
                    public List<AttributeConverterInfo> getAttributeConverters() {
                        return null;
                    }
                });
            }

            @Override
            public Collection<PersistentClass> getEntityBindings() {
                return null;
            }

            @Override
            public PersistentClass getEntityBinding(String s) {
                return null;
            }

            @Override
            public Collection<org.hibernate.mapping.Collection> getCollectionBindings() {
                return null;
            }

            @Override
            public org.hibernate.mapping.Collection getCollectionBinding(String s) {
                return null;
            }

            @Override
            public Map<String, String> getImports() {
                return null;
            }

            @Override
            public NamedQueryDefinition getNamedQueryDefinition(String s) {
                return null;
            }

            @Override
            public Collection<NamedQueryDefinition> getNamedQueryDefinitions() {
                return null;
            }

            @Override
            public NamedSQLQueryDefinition getNamedNativeQueryDefinition(String s) {
                return null;
            }

            @Override
            public Collection<NamedSQLQueryDefinition> getNamedNativeQueryDefinitions() {
                return null;
            }

            @Override
            public Collection<NamedProcedureCallDefinition> getNamedProcedureCallDefinitions() {
                return null;
            }

            @Override
            public ResultSetMappingDefinition getResultSetMapping(String s) {
                return null;
            }

            @Override
            public Map<String, ResultSetMappingDefinition> getResultSetMappingDefinitions() {
                return null;
            }

            @Override
            public TypeDefinition getTypeDefinition(String s) {
                return null;
            }

            @Override
            public Map<String, FilterDefinition> getFilterDefinitions() {
                return null;
            }

            @Override
            public FilterDefinition getFilterDefinition(String s) {
                return null;
            }

            @Override
            public FetchProfile getFetchProfile(String s) {
                return null;
            }

            @Override
            public Collection<FetchProfile> getFetchProfiles() {
                return null;
            }

            @Override
            public NamedEntityGraphDefinition getNamedEntityGraph(String s) {
                return null;
            }

            @Override
            public Map<String, NamedEntityGraphDefinition> getNamedEntityGraphs() {
                return null;
            }

            @Override
            public IdentifierGeneratorDefinition getIdentifierGenerator(String s) {
                return null;
            }

            @Override
            public Collection<Table> collectTableMappings() {
                return null;
            }

            @Override
            public Map<String, SQLFunction> getSqlFunctionMap() {
                return null;
            }

            @Override
            public MetadataBuildingOptions getMetadataBuildingOptions() {
                return null;
            }

            @Override
            public TypeConfiguration getTypeConfiguration() {
                return null;
            }

            @Override
            public TypeResolver getTypeResolver() {
                return null;
            }

            @Override
            public NamedQueryRepository buildNamedQueryRepository(SessionFactoryImpl sessionFactory) {
                return null;
            }

            @Override
            public void validate() throws MappingException {

            }

            @Override
            public Set<MappedSuperclass> getMappedSuperclassMappingsCopy() {
                return null;
            }
        };
        return new SessionFactoryImplAtiimp(new SessionFactoryImpl(BootstrapContext1,MetadataImplementor1,SessionFactoryOptions1));
    }

    public static SessionFactoryImplementor newSessionFactoryImplementor() {
        return new SessionFactoryImplementor(){
            @Override
            public Type resolveParameterBindType(Object o) {
                return null;
            }

            @Override
            public Type resolveParameterBindType(Class aClass) {
                return null;
            }

            @Override
            public IdentifierGeneratorFactory getIdentifierGeneratorFactory() {
                return null;
            }

            @Override
            public Type getIdentifierType(String s) throws MappingException {
                return null;
            }

            @Override
            public String getIdentifierPropertyName(String s) throws MappingException {
                return null;
            }

            @Override
            public Type getReferencedPropertyType(String s, String s1) throws MappingException {
                return null;
            }

            @Override
            public Reference getReference() throws NamingException {
                return null;
            }

            @Override
            public String getUuid() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public SessionFactoryOptions getSessionFactoryOptions() {
                return null;
            }

            @Override
            public SessionBuilderImplementor withOptions() {
                return null;
            }

            @Override
            public Session openSession() throws HibernateException {
                return null;
            }

            @Override
            public Session getCurrentSession() throws HibernateException {
                return null;
            }

            @Override
            public StatelessSessionBuilder withStatelessOptions() {
                return null;
            }

            @Override
            public StatelessSession openStatelessSession() {
                return null;
            }

            @Override
            public StatelessSession openStatelessSession(Connection connection) {
                return null;
            }

            @Override
            public Session openTemporarySession() throws HibernateException {
                return null;
            }

            @Override
            public CacheImplementor getCache() {
                return null;
            }

            @Override
            public PersistenceUnitUtil getPersistenceUnitUtil() {
                return null;
            }

            @Override
            public void addNamedQuery(String s, Query query) {

            }

            @Override
            public <T> T unwrap(Class<T> aClass) {
                return null;
            }

            @Override
            public <T> void addNamedEntityGraph(String s, EntityGraph<T> entityGraph) {

            }

            @Override
            public Set getDefinedFilterNames() {
                return null;
            }

            @Override
            public FilterDefinition getFilterDefinition(String s) throws HibernateException {
                return null;
            }

            @Override
            public boolean containsFetchProfileDefinition(String s) {
                return false;
            }

            @Override
            public TypeHelper getTypeHelper() {
                return null;
            }

            @Override
            public ClassMetadata getClassMetadata(Class aClass) {
                return null;
            }

            @Override
            public ClassMetadata getClassMetadata(String s) {
                return null;
            }

            @Override
            public CollectionMetadata getCollectionMetadata(String s) {
                return null;
            }

            @Override
            public Map<String, ClassMetadata> getAllClassMetadata() {
                return null;
            }

            @Override
            public Map getAllCollectionMetadata() {
                return null;
            }

            @Override
            public StatisticsImplementor getStatistics() {
                return null;
            }

            @Override
            public void close() throws HibernateException {

            }

            @Override
            public Map<String, Object> getProperties() {
                return null;
            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public ServiceRegistryImplementor getServiceRegistry() {
                return null;
            }

            @Override
            public Interceptor getInterceptor() {
                return null;
            }

            @Override
            public QueryPlanCache getQueryPlanCache() {
                return null;
            }

            @Override
            public NamedQueryRepository getNamedQueryRepository() {
                return null;
            }

            @Override
            public org.hibernate.engine.profile.FetchProfile getFetchProfile(String s) {
                return null;
            }

            @Override
            public TypeResolver getTypeResolver() {
                return null;
            }

            @Override
            public IdentifierGenerator getIdentifierGenerator(String s) {
                return null;
            }

            @Override
            public EntityNotFoundDelegate getEntityNotFoundDelegate() {
                return null;
            }

            @Override
            public SQLFunctionRegistry getSqlFunctionRegistry() {
                return null;
            }

            @Override
            public void addObserver(SessionFactoryObserver sessionFactoryObserver) {

            }

            @Override
            public CustomEntityDirtinessStrategy getCustomEntityDirtinessStrategy() {
                return null;
            }

            @Override
            public CurrentTenantIdentifierResolver getCurrentTenantIdentifierResolver() {
                return null;
            }

            @Override
            public DeserializationResolver getDeserializationResolver() {
                return null;
            }

            @Override
            public JdbcServices getJdbcServices() {
                return null;
            }

            @Override
            public Settings getSettings() {
                return null;
            }

            @Override
            public <T> List<EntityGraph<? super T>> findEntityGraphsByType(Class<T> aClass) {
                return null;
            }

            @Override
            public EntityManager createEntityManager() {
                return null;
            }

            @Override
            public EntityManager createEntityManager(Map map) {
                return null;
            }

            @Override
            public EntityManager createEntityManager(SynchronizationType synchronizationType) {
                return null;
            }

            @Override
            public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
                return null;
            }

            @Override
            public CriteriaBuilder getCriteriaBuilder() {
                return null;
            }

            @Override
            public MetamodelImplementor getMetamodel() {
                return new MetamodelImplementor(){
                    @Override
                    public <X> EntityType<X> entity(Class<X> aClass) {
                        return null;
                    }

                    @Override
                    public <X> ManagedType<X> managedType(Class<X> aClass) {
                        return null;
                    }

                    @Override
                    public <X> EmbeddableType<X> embeddable(Class<X> aClass) {
                        return null;
                    }

                    @Override
                    public Set<ManagedType<?>> getManagedTypes() {
                        return null;
                    }

                    @Override
                    public Set<EntityType<?>> getEntities() {
                        return null;
                    }

                    @Override
                    public Set<EmbeddableType<?>> getEmbeddables() {
                        return null;
                    }

                    @Override
                    public TypeConfiguration getTypeConfiguration() {
                        return null;
                    }

                    @Override
                    public SessionFactoryImplementor getSessionFactory() {
                        return null;
                    }

                    @Override
                    public <X> EntityType<X> entity(String s) {
                        return null;
                    }

                    @Override
                    public String getImportedClassName(String s) {
                        return null;
                    }

                    @Override
                    public String[] getImplementors(String s) {
                        return new String[0];
                    }

                    @Override
                    public Collection<EntityNameResolver> getEntityNameResolvers() {
                        return null;
                    }

                    @Override
                    public EntityPersister locateEntityPersister(Class aClass) {
                        return null;
                    }

                    @Override
                    public EntityPersister locateEntityPersister(String s) {
                        return null;
                    }

                    @Override
                    public EntityPersister entityPersister(Class aClass) {
                        return null;
                    }

                    @Override
                    public EntityPersister entityPersister(String s) {
                        return null;
                    }

                    @Override
                    public Map<String, EntityPersister> entityPersisters() {
                        return null;
                    }

                    @Override
                    public CollectionPersister collectionPersister(String s) {
                        return null;
                    }

                    @Override
                    public Map<String, CollectionPersister> collectionPersisters() {
                        return null;
                    }

                    @Override
                    public Set<String> getCollectionRolesByEntityParticipant(String s) {
                        return null;
                    }

                    @Override
                    public String[] getAllEntityNames() {
                        return new String[0];
                    }

                    @Override
                    public String[] getAllCollectionRoles() {
                        return new String[0];
                    }

                    @Override
                    public <T> void addNamedEntityGraph(String s, EntityGraph<T> entityGraph) {

                    }

                    @Override
                    public <T> EntityGraph<T> findEntityGraphByName(String s) {
                        return null;
                    }

                    @Override
                    public <T> List<EntityGraph<? super T>> findEntityGraphsByType(Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public void close() {

                    }
                };
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public EntityGraph findEntityGraphByName(String s) {
                return null;
            }
        };
    }


//       new SessionFactoryImpl(null,null,new AbstractDelegatingSessionFactoryOptions(){{
//
//    }
//
//    })

}
