/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one
 *  * or more contributor license agreements.  See the NOTICE file
 *  * distributed with this work for additional information
 *  * regarding copyright ownership.  The ASF licenses this file
 *  * to you under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License.  You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package org.apache.tinkerpop.gremlin.neo4j.structure.full;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.neo4j.process.traversal.strategy.optimization.Neo4jGraphStepStrategy;
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jEdge;
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jVertex;
import org.apache.tinkerpop.gremlin.process.traversal.TraversalStrategies;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.neo4j.tinkerpop.api.Neo4jGraphAPI;
import org.neo4j.tinkerpop.api.Neo4jNode;
import org.neo4j.tinkerpop.api.Neo4jRelationship;

import java.util.function.Predicate;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class FullNeo4jGraph extends Neo4jGraph {

    private static final Configuration EMPTY_CONFIGURATION = new BaseConfiguration() {{
        this.setProperty(Graph.GRAPH, FullNeo4jGraph.class.getName());
        this.setProperty(Neo4jGraph.CONFIG_META_PROPERTIES, true);
        this.setProperty(Neo4jGraph.CONFIG_MULTI_PROPERTIES, true);
    }};

    static {
        TraversalStrategies.GlobalCache.registerStrategies(FullNeo4jGraph.class, TraversalStrategies.GlobalCache.getStrategies(Graph.class).clone().addStrategies(Neo4jGraphStepStrategy.instance()));
    }

    public FullNeo4jGraph(final Configuration configuration) {
        super(configuration);
    }

    public FullNeo4jGraph(final Neo4jGraphAPI baseGraph) {
        super(baseGraph, EMPTY_CONFIGURATION);
    }

    @Override
    public Neo4jVertex createVertex(final Neo4jNode node) {
        return new FullNeo4jVertex(node, this);
    }

    @Override
    public Neo4jEdge createEdge(final Neo4jRelationship relationship) {
        return new Neo4jEdge(relationship, this);
    }

    @Override
    public Predicate<Neo4jNode> getNodePredicate() {
        return node -> !node.hasLabel(FullNeo4jVertexProperty.VERTEX_PROPERTY_LABEL);
    }

    @Override
    public Predicate<Neo4jRelationship> getRelationshipPredicate() {
        return relationship -> !relationship.type().startsWith(FullNeo4jVertexProperty.VERTEX_PROPERTY_PREFIX);
    }
}
