/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.medcontrol.aspects;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Statistics;
import net.sf.ehcache.Status;
import net.sf.ehcache.bootstrap.BootstrapCacheLoader;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.event.RegisteredEventListeners;
import net.sf.ehcache.exceptionhandler.CacheExceptionHandler;
import net.sf.ehcache.extension.CacheExtension;
import net.sf.ehcache.loader.CacheLoader;

public class MockCache implements Ehcache {

  protected Map<String, Element> elements = new HashMap<String, Element>();

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  @Override
  public void bootstrap() {
  }

  @Override
  public long calculateInMemorySize() throws IllegalStateException, CacheException {
    return 0;
  }

  @Override
  public void clearStatistics() {
  }

  @Override
  public void dispose() throws IllegalStateException {
  }

  @Override
  public void evictExpiredElements() {
  }

  @Override
  public void flush() throws IllegalStateException, CacheException {
  }

  @Override
  public Element get(Serializable key) throws IllegalStateException, CacheException {
    return elements.get(key);
  }

  @Override
  public Element get(Object key) throws IllegalStateException, CacheException {
    return null;
  }

  @Override
  public Map getAllWithLoader(Collection keys, Object loaderArgument) throws CacheException {
    return null;
  }

  @Override
  public float getAverageGetTime() {
    return 0;
  }

  @Override
  public BootstrapCacheLoader getBootstrapCacheLoader() {
    return null;
  }

  @Override
  public CacheConfiguration getCacheConfiguration() {
    return null;
  }

  @Override
  public RegisteredEventListeners getCacheEventNotificationService() {
    return null;
  }

  @Override
  public CacheExceptionHandler getCacheExceptionHandler() {
    return null;
  }

  @Override
  public CacheManager getCacheManager() {
    return null;
  }

  @Override
  public int getDiskStoreSize() throws IllegalStateException {
    return 0;
  }

  @Override
  public String getGuid() {
    return null;
  }

  @Override
  public List getKeys() throws IllegalStateException, CacheException {
    return null;
  }

  @Override
  public List getKeysNoDuplicateCheck() throws IllegalStateException {
    return null;
  }

  @Override
  public List getKeysWithExpiryCheck() throws IllegalStateException, CacheException {
    return null;
  }

  @Override
  public long getMemoryStoreSize() throws IllegalStateException {
    return 0;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public Element getQuiet(Serializable key) throws IllegalStateException, CacheException {
    return null;
  }

  @Override
  public Element getQuiet(Object key) throws IllegalStateException, CacheException {
    return null;
  }

  @Override
  public List<CacheExtension> getRegisteredCacheExtensions() {
    return null;
  }

  @Override
  public List<CacheLoader> getRegisteredCacheLoaders() {
    return null;
  }

  @Override
  public int getSize() throws IllegalStateException, CacheException {
    return 0;
  }

  @Override
  public Statistics getStatistics() throws IllegalStateException {
    return null;
  }

  @Override
  public int getStatisticsAccuracy() {
    return 0;
  }

  @Override
  public Status getStatus() {
    return null;
  }

  @Override
  public Element getWithLoader(Object key, CacheLoader loader, Object loaderArgument) throws CacheException {
    return null;
  }

  @Override
  public void initialise() {
  }

  @Override
  public boolean isDisabled() {
    return false;
  }

  @Override
  public boolean isElementInMemory(Serializable key) {
    return false;
  }

  @Override
  public boolean isElementInMemory(Object key) {
    return false;
  }

  @Override
  public boolean isElementOnDisk(Serializable key) {
    return false;
  }

  @Override
  public boolean isElementOnDisk(Object key) {
    return false;
  }

  @Override
  public boolean isExpired(Element element) throws IllegalStateException, NullPointerException {
    return false;
  }

  @Override
  public boolean isKeyInCache(Object key) {
    return false;
  }

  @Override
  public boolean isValueInCache(Object value) {
    return false;
  }

  @Override
  public void load(Object key) throws CacheException {
  }

  @Override
  public void loadAll(Collection keys, Object argument) throws CacheException {
  }

  @Override
  public void put(Element element) throws IllegalArgumentException, IllegalStateException, CacheException {
    elements.put((String)element.getObjectKey(), element);
  }

  @Override
  public void put(Element element, boolean doNotNotifyCacheReplicators) throws IllegalArgumentException, IllegalStateException, CacheException {
  }

  @Override
  public void putQuiet(Element element) throws IllegalArgumentException, IllegalStateException, CacheException {
  }

  @Override
  public void registerCacheExtension(CacheExtension cacheExtension) {
  }

  @Override
  public void registerCacheLoader(CacheLoader cacheLoader) {
  }

  @Override
  public boolean remove(Serializable key) throws IllegalStateException {
    return false;
  }

  @Override
  public boolean remove(Object key) throws IllegalStateException {
    return false;
  }

  @Override
  public boolean remove(Serializable key, boolean doNotNotifyCacheReplicators) throws IllegalStateException {
    return false;
  }

  @Override
  public boolean remove(Object key, boolean doNotNotifyCacheReplicators) throws IllegalStateException {
    return false;
  }

  @Override
  public void removeAll() throws IllegalStateException, CacheException {
  }

  @Override
  public void removeAll(boolean doNotNotifyCacheReplicators) throws IllegalStateException, CacheException {
  }

  @Override
  public boolean removeQuiet(Serializable key) throws IllegalStateException {
    return false;
  }

  @Override
  public boolean removeQuiet(Object key) throws IllegalStateException {
    return false;
  }

  @Override
  public void setBootstrapCacheLoader(BootstrapCacheLoader bootstrapCacheLoader) throws CacheException {
  }

  @Override
  public void setCacheExceptionHandler(CacheExceptionHandler cacheExceptionHandler) {
  }

  @Override
  public void setCacheManager(CacheManager cacheManager) {
  }

  @Override
  public void setDisabled(boolean disabled) {
  }

  @Override
  public void setDiskStorePath(String diskStorePath) throws CacheException {
  }

  @Override
  public void setName(String name) {
  }

  @Override
  public void setStatisticsAccuracy(int statisticsAccuracy) {
  }

  @Override
  public void unregisterCacheExtension(CacheExtension cacheExtension) {
  }

  @Override
  public void unregisterCacheLoader(CacheLoader cacheLoader) {
  }
  
  

}
